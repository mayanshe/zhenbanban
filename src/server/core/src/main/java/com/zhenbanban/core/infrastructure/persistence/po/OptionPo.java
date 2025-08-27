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
package com.zhenbanban.core.infrastructure.persistence.po;

import lombok.*;

/**
 * Po : 系统配置
 *
 * @author zhangxihai 2025/8/23
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OptionPo {
    private Long id;                       // 主键ID

    private String optionName;            // 配置项名称（如：网站标题、用户头像等）

    private String displayName;            // 显示名称（如：网站标题、用户头像等）

    private String optionValue;           // 配置项的值（如：网站标题的具体内容）

    private String description;            // 描述信息（如：网站标题的描述）

    @Builder.Default
    private short customized = 0;          // 是否为自定义配置项

    @Builder.Default
    private long createdAt = 0;            // 创建时间戳

    @Builder.Default
    private long updatedAt = 0;           // 更新时间戳

}
