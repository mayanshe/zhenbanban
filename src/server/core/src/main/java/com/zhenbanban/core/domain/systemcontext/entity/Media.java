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
package com.zhenbanban.core.domain.systemcontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.systemcontext.event.MediaAddEvent;
import com.zhenbanban.core.domain.systemcontext.event.MediaDestroyedEvent;
import com.zhenbanban.core.domain.systemcontext.event.MediaModifiedEvent;
import lombok.*;

/**
 * Aggregate : 媒体资源
 *
 * @author zhangxihai 2025/8/26
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Media extends AbsAggregate {
    private Long id;                       // 主键ID

    private String fileMd5;                // 文件MD5值

    private String fileName;               // 文件名称

    private String filePath;               // 文件路径

    private Long fileSize;                 // 文件大小，单位：字节

    private String fileExtension;          // 文件扩展名（如：jpg、png等）

    private String mimeType;               // 文件MIME类型（如：image/jpeg、image/png等）

    @Builder.Default
    private String url = "";               // 文件访问URL

    @Builder.Default
    private String thumbnailUrl = "";      // 缩略图URL（如果适用）

    @Builder.Default
    private String description = "";       // 文件描述信息

    @Builder.Default
    private boolean deleted = false;       // 是否已删除，true表示已删除，false表示未删除

    /**
     * 添加
     */
    public void add() {
        this.setDeleted(false);

        MediaAddEvent event = MediaAddEvent.builder()
                .mediaId(this.id)
                .fileMd5(this.fileMd5)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .fileSize(this.fileSize)
                .fileExtension(this.fileExtension)
                .mimeType(this.mimeType)
                .url(this.url)
                .thumbnailUrl(this.thumbnailUrl)
                .build();
        this.addEvent(event);
    }

    /**
     * 修改
     */
    public void modify() {
        this.setDeleted(false);

        MediaModifiedEvent event = MediaModifiedEvent.builder()
                .mediaId(this.id)
                .fileMd5(this.fileMd5)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .fileSize(this.fileSize)
                .fileExtension(this.fileExtension)
                .mimeType(this.mimeType)
                .url(this.url)
                .thumbnailUrl(this.thumbnailUrl)
                .build();
        this.addEvent(event);
    }

    /**
     * 删除
     */
    public void destroy() {
        this.setDeleted(true);

        MediaDestroyedEvent event = MediaDestroyedEvent.builder()
                .mediaId(this.id)
                .fileMd5(this.fileMd5)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .url(this.url)
                .thumbnailUrl(this.thumbnailUrl)
                .build();
        this.addEvent(event);
    }

}
