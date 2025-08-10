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
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Po: 角色
 *
 * @author zhangxihai 2025/08/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RolePo {
    private Long id;                                       // 角色ID

    private String roleName;                               // 角色名称

    private String displayName;                            // 角色显示名称

    private String description;                            // 角色描述

    @Builder.Default
    private long createdAt = 0L;                           // 创建时间戳

    @Builder.Default
    private long updatedAt = 0L;                           // 更新时间戳

    @Builder.Default
    private List<PermissionPo> permissions = List.of();    // 角色权限列表

    @Builder.Default
    private List<ResourcePo> resources = List.of();        // 角色资源列表

}
