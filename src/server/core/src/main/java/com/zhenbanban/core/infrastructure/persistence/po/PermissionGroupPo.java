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

import java.util.ArrayList;
import java.util.List;

/**
 * 类型 : PermissionGroupPo
 *
 * @author zhangxihai 2025/8/03
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroupPo {
    private Long id;                                                  // 权限组ID

    @Builder.Default
    private Long parentId = 0L;                                       // 父权限组ID, 0表示顶级权限组

    private String groupName;                                         // 权限组名称

    private String displayName;                                       // 权限组显示名称

    @Builder.Default
    private String description = "";                                  // 权限组描述

    @Builder.Default
    private int sort = 0;                                             // 排序，倒叙

    @Builder.Default
    private long createdAt = 0L;                                      // 创建时间戳, 默认0表示未设置

    @Builder.Default
    private long updatedAt = 0L;                                      // 更新时间戳, 默认0表示未设置

    @Builder.Default
    private List<PermissionGroupPo> children = new ArrayList<>();     // 子权限组列表, 默认空列表

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public List<PermissionGroupPo> getChildren() {
        return children != null ? children : new ArrayList<>();
    }

}
