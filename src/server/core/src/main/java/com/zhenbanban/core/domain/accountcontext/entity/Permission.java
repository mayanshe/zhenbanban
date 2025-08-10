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
package com.zhenbanban.core.domain.accountcontext.entity;

import com.zhenbanban.core.domain.accountcontext.event.PermissionAddedEvent;
import com.zhenbanban.core.domain.accountcontext.event.PermissionDestroyedEvent;
import com.zhenbanban.core.domain.accountcontext.event.PermissionModifiedEvent;
import com.zhenbanban.core.domain.common.AbsAggregate;
import lombok.*;

/**
 * 聚合根：权限
 *
 * @author zhangxihai 2025/08/02
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends AbsAggregate {
    private Long id;                      // 权限ID

    private Long groupId;                // 权限分组ID

    private String permissionName;       // 权限名称

    private String displayName;           // 权限显示名称

    private String description;           // 权限描述

    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加权限
     */
    public void add() {
        this.setDeleted(false);

        PermissionAddedEvent event = PermissionAddedEvent.builder()
                .refId(this.getId())
                .permissionId(this.getId())
                .permissionName(this.getPermissionName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 修改权限
     */
    public void modify() {
        this.setDeleted(false);

        PermissionModifiedEvent event = PermissionModifiedEvent.builder()
                .refId(this.getId())
                .permissionId(this.getId())
                .permissionName(this.getPermissionName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 销毁权限
     */
    public void destroy() {
        this.setDeleted(true);

        PermissionDestroyedEvent event = PermissionDestroyedEvent.builder()
                .refId(this.getId())
                .permissionId(this.getId())
                .permissionName(this.getPermissionName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

}
