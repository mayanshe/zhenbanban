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

import lombok.*;

import java.io.Serializable;

/**
 * ValueObject : 对象存储服务（COS）配置
 * 腾讯云对象存储服务（COS）配置类，包含SecretId等信息。
 *
 * @author zhangxihai 2025/8/24
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CosSetting implements Serializable {
    @Builder.Default
    private String uid = "";                        // 用户ID

    @Builder.Default
    private String cdnDomain = "";                  // CDN域名

    @Builder.Default
    private String secretId = "";                   // SecretId

    @Builder.Default
    private String secretKey = "";                  // SecretKey

    @Builder.Default
    private String region = "";                      // 区域

    @Builder.Default
    private String bucket = "";                      // 公读私写

    @Builder.Default
    private String privateBucket = "";                // 私读私写

    @Builder.Default
    private int durationSeconds = 3600;              // 临时密钥有效期，单位：秒，默认1800秒

    private transient boolean configured = false;    // 是否已配置

    public boolean isConfigured() {
        return !this.uid.isBlank() && !this.secretId.isBlank() && !this.secretKey.isBlank()
                && !this.region.isBlank() && !this.bucket.isBlank();
    }

    public void verify() {
        if (!isConfigured()) throw new IllegalStateException("COS未配置完整");
    }

    public static CosSetting defaults() {
        return CosSetting.builder()
                .build();
    }

}
