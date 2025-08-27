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

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Query : 自定义选项查询
 *
 * @author zhangxihai 2025/8/23
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedOptionQuery {
    @Builder.Default
    private int page = 1;          // 页码，默认值为1

    @Builder.Default
    private int pageSize = 15;     // 每页大小，默认值为15

    private String id;             // 配置ID

    private String keywords;       // 关键词

    public Map<String, Object> toMap() {
        Map<String, Object> map = new java.util.HashMap<>();
        if (id != null && !id.isBlank()) {
            map.put("id", id);
        }
        if (keywords != null && !keywords.isBlank()) {
            map.put("keywords", keywords);
        }
        return map;
    }

}
