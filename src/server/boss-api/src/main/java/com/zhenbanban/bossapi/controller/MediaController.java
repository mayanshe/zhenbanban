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
package com.zhenbanban.bossapi.controller;

import com.zhenbanban.bossapi.vo.MediaModifyRequest;
import com.zhenbanban.core.application.command.MediaAmdCmdHandler;
import com.zhenbanban.core.application.dto.MediaCreateCommand;
import com.zhenbanban.core.application.dto.MediaDto;
import com.zhenbanban.core.application.dto.MediaModifyCommand;
import com.zhenbanban.core.application.dto.MediaQuery;
import com.zhenbanban.core.application.query.MediaQueryHandler;
import com.zhenbanban.core.infrastructure.external.cos.CosProvider;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.infrastructure.util.ByteUtils;
import com.zhenbanban.core.infrastructure.util.StrUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller : 媒体资源
 *
 * @author zhangxihai 2025/8/26
 */
@RestController
@RequestMapping("/medias")
public class MediaController {
    private final CosProvider cosProvider;

    private final MediaQueryHandler mediaQueryHandler;

    private final MediaAmdCmdHandler mediaAmdCmdHandler;

    @Autowired
    public MediaController(
            @Lazy CosProvider cosProvider,
            @Lazy MediaQueryHandler mediaQueryHandler,
            @Lazy MediaAmdCmdHandler mediaAmdCmdHandler
    ) {
        this.cosProvider = cosProvider;
        this.mediaQueryHandler = mediaQueryHandler;
        this.mediaAmdCmdHandler = mediaAmdCmdHandler;
    }

    /**
     * 上传媒体资源文件
     *
     * @param file 媒体资源文件
     * @return 媒体资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit(permissions = {"media:add"}, message = "您未被授权执行此操作：添加媒体资源文件")
    public MediaDto upload(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") Boolean isPublicRead) throws IOException {
        return uploadFile(file, isPublicRead != null && isPublicRead);
    }

    /**
     * 上传图片文件，仅允许jpg|jpeg|png|gif|bmp格式
     *
     * @param file 图片文件
     * @return 图片资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit
    public MediaDto uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") Boolean isPublicRead) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!contentType.startsWith("image/") && !fileExtension.matches("jpg|jpeg|png|gif|bmp")) {
            throw new BadRequestException("文件类型错误，只允许jpg|jpeg|png|gif|bmp图片格式");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException("图片文件大小不能超过5MB");
        }

        return uploadFile(file, isPublicRead != null && isPublicRead);
    }

    /**
     * 上传视频文件，仅允许mp4|avi|mov|wmv|mkv格式
     *
     * @param file 视频文件
     * @return 视频资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit
    public MediaDto updateVideo(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") Boolean isPublicRead) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!contentType.startsWith("video/") && !fileExtension.matches("mp4|avi|mov|wmv|mkv")) {
            throw new BadRequestException("文件类型错误，只允许mp4|avi|mov|wmv|mkv视频格式");
        }

        if (file.getSize() > 100 * 1024 * 1024) {
            throw new BadRequestException("视频文件大小不能超过100MB");
        }

        return uploadFile(file, isPublicRead != null && isPublicRead);
    }

    /**
     * 上传音频文件，仅允许mp3|wav|ogg|aac|flac格式
     *
     * @param file 音频文件
     * @return 音频资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "/audios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit
    public MediaDto updateAudio(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") Boolean isPublicRead) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!contentType.startsWith("audio/") && !fileExtension.matches("mp3|wav|ogg|aac|flac")) {
            throw new BadRequestException("文件类型错误，只允许mp3|wav|ogg|aac|flac音频格式");
        }

        if (file.getSize() > 50 * 1024 * 1024) {
            throw new BadRequestException("音频文件大小不能超过50MB");
        }

        return uploadFile(file, isPublicRead != null && isPublicRead);
    }

    /**
     * 上传文档文件，仅允许pdf|doc|docx|xls|xlsx|ppt|pptx|txt格式
     *
     * @param file 文档文件
     * @return 文档资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit
    public MediaDto updateDocument(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") Boolean isPublicRead) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!contentType.contains("pdf") && !fileExtension.matches("pdf|doc|docx|xls|xlsx|ppt|pptx|txt")) {
            throw new BadRequestException("文件类型错误，只允许pdf|doc|docx|xls|xlsx|ppt|pptx|txt文档格式");
        }

        if (file.getSize() > 20 * 1024 * 1024) {
            throw new BadRequestException("文档文件大小不能超过20MB");
        }

        return uploadFile(file, isPublicRead != null && isPublicRead);
    }

    /**
     * 上传压缩包文件，仅允许zip|rar|7z|tar|gz格式
     *
     * @param file 压缩包文件
     * @return 压缩包资源ID响应
     * @throws IOException IO异常
     */
    @PostMapping(value = "/archives", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminPermit
    public MediaDto uploadArchive(@RequestParam("file") MultipartFile file, @RequestParam("isPublicRead") boolean isPublicRead) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        if (!contentType.contains("zip") && !fileExtension.matches("zip|rar|7z|tar|gz")) {
            throw new BadRequestException("文件类型错误，只允许zip|rar|7z|tar|gz压缩包格式");
        }

        if (file.getSize() > 200 * 1024 * 1024) {
            throw new BadRequestException("压缩包文件大小不能超过200MB");
        }

        return uploadFile(file, isPublicRead);
    }

    /**
     * 修改媒体资源信息
     *
     * @param id      媒体资源ID
     * @param request 修改请求体
     */
    @PatchMapping("/{id}")
    @AdminPermit(permissions = {"media:modify"}, message = "您未被授权执行此操作：修改媒体资源信息")
    public void modifyMedia(@PathVariable("id") Long id, @RequestBody MediaModifyRequest request) {
        MediaModifyCommand command = MediaModifyCommand.builder()
                .id(id)
                .fileName(request.getFileName() != null ? request.getFileName() : "")
                .description(request.getDescription() != null ? request.getDescription() : "")
                .build();
        mediaAmdCmdHandler.handleModify(command);
    }

    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"media:delete"}, message = "您未被授权执行此操作：删除媒体资源")
    public void deleteMedia(@PathVariable("id") Long id) {
        mediaAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取媒体资源详情
     *
     * @param id 媒体资源ID
     * @return 媒体资源详情
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"media:add", "media:modify", "media:delete"}, message = "您未被授权执行此操作：查看媒体资源详情")
    public MediaDto getMedia(@PathVariable("id") Long id) {
        return mediaQueryHandler.handleQuerySingle(id);
    }

    @GetMapping
    @AdminPermit(permissions = {"media:add", "media:modify", "media:delete"}, message = "您未被授权执行此操作：查看媒体资源列表")
    public Pagination<MediaDto> getMediaPagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "mediaType", defaultValue = "", required = false) String mediaType
    ) {
        MediaQuery query = MediaQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .mediaType(mediaType)
                .build();
        return mediaQueryHandler.handleQueryPage(query);
    }

    /**
     * 上传文件的通用方法
     *
     * @param file 上传的文件
     * @return 文件资源ID响应
     * @throws IOException IO异常
     */
    private MediaDto uploadFile(MultipartFile file, boolean isPublicRead) throws IOException {
        String fileMd5 = StrUtils.convertToMd5(String.format("%s:%s", isPublicRead ? "public" : "private", ByteUtils.bytesToHex(file.getBytes())));

        // 检查文件是否已存在
        MediaDto dto = mediaQueryHandler.handleQueryByMd5(fileMd5);
        if (dto != null) {
            return dto;
        }

        long fileSize = file.getSize();
        String contentTpe = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        String fileExtension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase() : "";
        String filePath = cosProvider.save(file.getBytes(), originalFilename, contentTpe, isPublicRead);
        String fileUrl = cosProvider.getFileUrl(filePath);
        String fileType = determineMediaType(contentTpe, fileExtension);
        String fileName = originalFilename.substring(originalFilename.lastIndexOf('/') + 1, originalFilename.lastIndexOf('.'));

        MediaCreateCommand command = MediaCreateCommand.builder()
                .mimeType(fileType)
                .fileMd5(fileMd5)
                .fileName(fileName)
                .filePath(filePath)
                .fileSize(fileSize)
                .fileExtension(fileExtension)
                .mimeType(contentTpe)
                .url(fileUrl)
                .thumbnailUrl("")
                .description("")
                .build();

        return mediaAmdCmdHandler.handleAdd(command);
    }

    /**
     * 根据MIME类型和文件扩展名确定媒体类型
     *
     * @param mimeType  MIME类型
     * @param extension 文件扩展名
     * @return 媒体类型（image, video, audio, document, archive, other）
     */
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
