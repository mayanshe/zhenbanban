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

import com.zhenbanban.core.infrastructure.support.annotation.InList;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request : 资源
 *
 * @author zhangxihai 2025/8/03
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSaveRequest {
    @Builder.Default
    private Long parentId = 0L;               // 父资源ID

    @NotBlank(message = "请选择资源类型")
    @InList(value = {"MENU", "COMPONENT", "LINK", "BUTTON"}, message = "请选择正确的资源类型")
    private String resourceType;              // 资源类型

    @NotBlank(message = "资源名称不能为空")
    @Size(max = 75, message = "资源名称不能超过75个字符")
    @Pattern(regexp = "^[a-zA-Z\\-:]+$", message = "权限名称只能包含字母和-:")
    private String resourceName;              // 资源名称

    @NotBlank(message = "资源显示名称不能为空")
    @Size(max = 75, message = "资源显示名称不能超过75个字符")
    private String displayName;               // 资源显示名称

    @Builder.Default
    @Size(max = 255, message = "资源描述不能超过255个字符")
    private String description = "";          // 资源描述

    @Builder.Default
    @Size(max = 255, message = "资源路径不能超过255个字符")
    private String path = "";                  // 资源路径

    @Builder.Default
    @Size(max = 255, message = "资源URL不能超过255个字符")
    private String url = "";                  // 资源URL

    @Builder.Default
    @Size(max = 75, message = "资源图标不能超过255个字符")
    private String icon = "";                 // 资源图标

    @Builder.Default
    @Size(max = 255, message = "资源组件路径不能超过255个字符")
    private String component = "";            // 资源组件路径

    @Builder.Default
    private boolean showInMenu = false;       // 是否在菜单中显示，默认不显示

    @Builder.Default
    @Min(value = 0, message = "排序值不能小于0")
    private int sort = 0;                     // 资源排序，默认0表示不排序


}
