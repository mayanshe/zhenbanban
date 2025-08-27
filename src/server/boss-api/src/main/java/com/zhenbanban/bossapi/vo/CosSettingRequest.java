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
package com.zhenbanban.bossapi.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request : Cos对象存储服务配置请求
 *
 * @author zhangxihai 2025/8/24
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CosSettingRequest {
    @NotBlank(message = "用户ID不能为空")
    private String uid;                            // 用户ID

    private String cdnDomain = "";                 // CDN域名

    @Builder.Default
    @NotBlank(message = "SecretId不能为空")
    private String secretId = "";                 // SecretId

    @Builder.Default
    @NotBlank(message = "SecretKey不能为空")
    private String secretKey = "";                 // SecretKey

    @Builder.Default
    @NotBlank(message = "区域不能为空")
    private String region = "";                    // 区域

    @Builder.Default
    @NotBlank(message = "公读存储桶名称不能为空")
    private String bucket = "";                    // 存储桶名称

    @Builder.Default
    @NotBlank(message = "私读读存储桶名称不能为空")
    private String privateBucket = "";              // 私读私写存储桶名称

    @Builder.Default
    @Min(value = 900, message = "临时密钥有效期不能小于900秒")
    @Max(value = 7200, message = "临时密钥有效期不能大于7200秒")
    private Integer durationSeconds = 3600;        // 临时密钥有效期，单位：秒，默认1800秒

}
