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
package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.infrastructure.util.DateUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Dto : 中医证候
 *
 * @author zhangxihai 2025/09/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SyndromeDto {
    private String id;                             // 主键ID

    private String syndromeCode;                   // 证侯编码

    private String syndromeName;                   // 证侯名称

    private String syndromeNamePinyin;             // 证侯名称拼音

    private String syndromeNamePinyinAbbr;         // 证侯名称拼音首字母缩写

    private String description;                    // 证侯描述

    private String createdAt;                      // 创建时间

    private String updatedAt;                      // 最后修改时间

    private String deletedAt;                      // 删除时间 0-未删除，非0-删除时间

    public String getCreatedAt() {
        return createdAt == null || createdAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt == null || updatedAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(updatedAt);
    }

    public String getDeletedAt() {
        return deletedAt == null || deletedAt.isBlank() || "0".equals(deletedAt) ? "" : DateUtils.timestampToFormattedDate(deletedAt);
    }

}
