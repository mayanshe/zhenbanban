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

import java.util.List;

/**
 * 类型 : GroupedPermissionDto
 *
 * @author zhangxihai 2025/8/19
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupedPermissionDto {
    private String groupId;                                     // 权限组ID

    private String groupName;                                   // 权限组名称

    private String groupDisplayName;                            // 权限组显示名称

    @Builder.Default
    private boolean collapse = false;

    @Builder.Default
    private List<GroupedPermissionDto> children = List.of();   // 子权限组列表

    @Builder.Default
    private List<PermissionDto> permissions = List.of();       // 权限列表

}
