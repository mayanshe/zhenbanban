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

/**
 * 类型 : OptionDto
 *
 * @author zhangxihai 2025/8/23
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private String id;              // 选项ID

    private String optionName;      // 选项名称

    private String displayName;     // 选项键

    private String optionValue;     // 选项值

    private String description;     // 选项描述

    private String createdAt;       // 创建时间

    private String updatedAt;       // 更新时间

    public String getCreatedAt() {
        return createdAt == null || createdAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt == null || updatedAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(updatedAt);
    }

}
