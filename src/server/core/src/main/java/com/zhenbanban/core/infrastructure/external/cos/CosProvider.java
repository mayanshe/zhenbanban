/*
 * Copyright (C) 2025 zhangxihai<mail@sniu.com>，All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * WARNING: This code is licensed under the GPL. Any derivative work or
 * distribution of this code must also be licensed under the GPL. Failure
 * to comply with the terms of the GPL may result in legal action.
 */
package com.zhenbanban.core.infrastructure.external.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.*;
import com.zhenbanban.core.infrastructure.persistence.mapper.OptionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.OptionPo;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import com.zhenbanban.core.shared.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.TreeMap;

import com.tencent.cloud.cos.util.Jackson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;

/**
 * 扩展 : 对象存储服务（COS）提供者
 *
 * @author zhangxihai 2025/8/25
 */
@Component
@RequiredArgsConstructor
public class CosProvider {

    private final OptionPoMapper optionPoMapper;

    private final RedisUtils redisUtils;

   /**
     * 保存文件
     *
     * @param data             文件数据
     * @param originalFilename 原始文件名
     * @param contentType      文件类型
     * @param isPublicRead     是否公读
     * @return 文件路径
     */
    public String save(byte[] data, String originalFilename, String contentType, boolean isPublicRead) {
        CosSetting setting = getClientSetting();
        setting.verify();
        COSClient cosClient = getCosClient(setting);

        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String randomStr = UUID.randomUUID().toString().replace("-", "");
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String type = determineMediaType(contentType, extension);
            String key = String.format("/%s/%s/%s%s", type.toLowerCase(), dateStr.toLowerCase(), randomStr.toLowerCase(), extension.toLowerCase());
            String bucket = isPublicRead ? setting.getBucket() : setting.getPrivateBucket();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream, null);
            PutObjectResult result = cosClient.putObject(putObjectRequest);

            return String.format("%s:%s", isPublicRead ? "public" : "private", key);
        } catch (Exception e) {
            throw new BadRequestException("上传文件失败: " + e.getMessage());
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public void delete(String filePath) {
        if (filePath.startsWith("public:") || filePath.startsWith("private:")) {
            boolean isPublicRead = filePath.startsWith("public:");
            filePath = filePath.substring(isPublicRead ? 7 : 8);
            CosSetting setting = getClientSetting();
            COSClient cosClient = getCosClient(setting);

            String bucket = isPublicRead ? setting.getBucket() : setting.getPrivateBucket();
            try {
                cosClient.deleteObject(bucket, filePath);
            } catch (Exception e) {
                throw new InternalServerException("删除文件失败: " + e.getMessage());
            } finally {
                cosClient.shutdown();
            }
        } else {
            throw new IllegalArgumentException("无效的文件路径前缀");
        }
    }

    /**
     * 获取文件访问URL
     *
     * @param filePath 文件路径
     * @return 文件访问URL
     */
    public String getFileUrl(String filePath) {
        if (filePath.startsWith("public:")) {
            filePath = filePath.substring(7);
            CosSetting setting = getClientSetting();

            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }

            if (setting.getCdnDomain() != null && !setting.getCdnDomain().isBlank()) {
                return String.format("https://%s/%s", setting.getCdnDomain(), filePath);
            } else {
                return String.format("https://%s.cos.%s.myqcloud.com/%s", setting.getBucket(), setting.getRegion(), filePath);
            }
        } else if (filePath.startsWith("private:")) {
            filePath = filePath.substring(8);
            CosSetting setting = getClientSetting();
            COSClient cosClient = getCosClient(setting);

            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(setting.getPrivateBucket(), filePath);
            req.setExpiration(expiration);

            URL signedUrl = cosClient.generatePresignedUrl(req);
            cosClient.shutdown();

            return signedUrl.toString();
        } else {
            throw new IllegalArgumentException("无效的文件路径前缀");
        }
    }

    /**
     * 获取COS客户端
     *
     * @return COS客户端
     */
    private COSClient getCosClient(CosSetting setting) {
        Credentials credentials = getCredentials(setting);
        BasicSessionCredentials cred = new BasicSessionCredentials(credentials.tmpSecretId, credentials.tmpSecretKey, credentials.sessionToken);

        Region region = new Region(setting.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setMaxErrorRetry(4);

        return new COSClient(cred, clientConfig);
    }

    /**
     * 获取COS临时密钥
     *
     * @return COS临时密钥
     */
    private Credentials getCredentials(CosSetting setting) {
        String cacheKey = CacheKeyGenerator.getCosCredentialsKey();
        if (redisUtils.hasKey(cacheKey)) {
            Credentials credentials = redisUtils.get(cacheKey, Credentials.class, null);
            if (credentials != null) {
                return credentials;
            }
        }

        TreeMap<String, Object> config = new TreeMap<>();
        try {
            Statement statement = new Statement();
            statement.setEffect("allow");
            statement.addActions(new String[]{
                    "cos:PutObject",
                    "cos:DeleteObject",
                    "cos:DeleteMultipleObjects"
            });
            statement.addResources(new String[]{
                    String.format("qcs::cos:%s:uid/%s:%s/*", setting.getRegion(), setting.getUid(), setting.getBucket()),
                    String.format("qcs::cos:%s:uid/%s:%s/*", setting.getRegion(), setting.getUid(), setting.getPrivateBucket())
            });

            Policy policy = new Policy();
            policy.addStatement(statement);

            config.put("secretId", setting.getSecretId());
            config.put("secretKey", setting.getSecretKey());
            config.put("durationSeconds", setting.getDurationSeconds());
            config.put("bucket", setting.getBucket());
            config.put("region", setting.getRegion());
            config.put("policy", Jackson.toJsonPrettyString(policy));

            Response response = CosStsClient.getCredential(config);

            storeCredentialsCache(response.credentials, setting.getDurationSeconds());

            return response.credentials;
        } catch (Exception e) {
            throw new InternalServerException("获取COS对象存储临时密钥失败" + e.getMessage());
        }
    }

    /**
     * 获取COS客户端配置
     *
     * @return COS客户端配置
     */
    private CosSetting getClientSetting() {
        String cacheKey = CacheKeyGenerator.getCosSettingKey();
        if (redisUtils.hasKey(cacheKey)) {
            CosSetting setting = redisUtils.get(cacheKey, CosSetting.class, null);
            if (setting != null) {
                return setting;
            }
        }

        OptionPo po = optionPoMapper.findByOptionName("cos-setting");
        if (po == null || po.getOptionValue().isBlank()) {
            throw new IllegalStateException("尚未配置COS对象存储");
        }

        byte[] bytes = Base64.getDecoder().decode(po.getOptionValue());
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            CosSetting setting =  (CosSetting) ois.readObject();
            storeCosSettingCache(setting);
            return setting;
        } catch (Exception e) {
            throw new InternalServerException("获取COS对象存储配置信息失败");
        }
    }

    /**
     * 缓存COS配置
     * @param setting COS配置
     */
    @Async
    private void storeCosSettingCache(CosSetting setting) {
        String cacheKey = CacheKeyGenerator.getCosSettingKey();
        redisUtils.set(cacheKey, setting, 1296000);
    }

    /**
     * 缓存COS临时密钥
     * @param credentials COS临时密钥
     * @param durationSeconds 有效期，单位：秒
     */
    @Async
    private void storeCredentialsCache(Credentials credentials, int durationSeconds) {
        String cacheKey = CacheKeyGenerator.getCosCredentialsKey();
        redisUtils.set(cacheKey, credentials, durationSeconds - 300);
    }

    private String determineMediaType(String mimeType, String extension) {
        mimeType = mimeType.toLowerCase();
        extension = extension.toLowerCase();
        if (mimeType.startsWith("image/") || extension.matches("jpg|jpeg|png|gif|bmp|webp")) {
            return "image";
        } else if (mimeType.startsWith("video/") || extension.matches("mp4|avi|mov|wmv|mkv")) {
            return "video";
        } else if (mimeType.startsWith("audio/") || extension.matches("mp3|wav|ogg|aac|flac")) {
            return "audio";
        } else if (mimeType.contains("pdf") || extension.matches("pdf|doc|docx|xls|xlsx|ppt|pptx|txt")) {
            return "document";
        } else if (mimeType.contains("zip") || extension.matches("zip|rar|7z|tar|gz")) {
            return "archive";
        } else {
            return "other";
        }
    }

}
