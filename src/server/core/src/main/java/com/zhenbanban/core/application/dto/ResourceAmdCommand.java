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

import com.zhenbanban.core.application.common.BaseCommand;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Command : 资源
 *
 * @author zhangxihai 2025/8/03
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAmdCommand extends BaseCommand<Long> {
    @Builder.Default
    private Long parentId = 0L;           // 父资源ID

    private String resourceType;           // 资源类型

    private String resourceName;           // 资源名称

    private String displayName;            // 资源显示名称

    @Builder.Default
    private String description = "";       // 资源描述

    @Builder.Default
    private String url = "";               // 资源URL

    @Builder.Default
    private String icon = "";              // 资源图标

    @Builder.Default
    private String component = "";         // 资源组件路径\

    @Builder.Default
    private boolean showInMenu = false;    // 是否在菜单中显示，默认不显示

    @Builder.Default
    private int sort = 0;                  // 资源排序，默认0表示不排序

}
