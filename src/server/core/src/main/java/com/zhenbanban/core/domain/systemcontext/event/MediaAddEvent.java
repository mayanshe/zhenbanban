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
package com.zhenbanban.core.domain.systemcontext.event;

import com.zhenbanban.core.domain.common.AbsDomainEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Event : 媒体资源创建成功事件
 *
 * @author zhangxihai 2025/8/26
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MediaAddEvent extends AbsDomainEvent {
    private Long mediaId;                  // 媒体资源ID

    private String fileMd5;                // 文件MD5值

    private String fileName;               // 文件名称

    private String filePath;               // 文件路径

    private Long fileSize;                 // 文件大小，单位：字节

    private String fileExtension;          // 文件扩展名（如：jpg、png等）

    private String mimeType;               // 文件MIME类型（如：image/jpeg、image/png等）

    private String url;                    // 文件访问URL

    private String thumbnailUrl;           // 缩略图URL（如果适用）

}
