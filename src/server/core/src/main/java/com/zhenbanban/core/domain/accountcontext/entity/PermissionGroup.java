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

import com.zhenbanban.core.domain.accountcontext.event.PermissionGroupAddedEvent;
import com.zhenbanban.core.domain.accountcontext.event.PermissionGroupDestroyedEvent;
import com.zhenbanban.core.domain.accountcontext.event.PermissionGroupModifiedEvent;
import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.*;

import java.util.List;

/**
 * 聚合根 : 全线组
 *
 * @author zhangxihai 2025/8/03
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroup extends AbsAggregate {
    private Long id;                       // 权限组ID

    private Long parentId;                 // 租户ID

    private String groupName;              // 权限组名称

    private String displayName;            // 权限组显示名称

    private String description;            // 权限组描述

    private int sort;                      // 排序

    @Builder.Default
    private boolean deleted = false;       // 是否已删除

    private List<Long> childrenIds;        // 子权限组ID列表

    /**
     * 添加权限组
     */
    public void add() {
        this.setDeleted(false);

        PermissionGroupAddedEvent event = PermissionGroupAddedEvent.builder()
                .refId(this.getId())
                .permissionGroupId(this.getId())
                .parentId(this.getParentId())
                .groupName(this.getGroupName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 修改权限组
     */
    public void modify() {
        this.setDeleted(false);

        PermissionGroupModifiedEvent event = PermissionGroupModifiedEvent.builder()
                .refId(this.getId())
                .permissionGroupId(this.getId())
                .parentId(this.getParentId())
                .groupName(this.getGroupName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 删除权限组
     */
    public void destroy() {
        if (this.childrenIds != null && !this.childrenIds.isEmpty()) {
            throw new BadRequestException("无法删除权限组，存在子权限组，请先删除子权限组");
        }

        this.setDeleted(true);
        PermissionGroupDestroyedEvent event = PermissionGroupDestroyedEvent.builder()
                .refId(this.getId())
                .permissionGroupId(this.getId())
                .parentId(this.getParentId())
                .groupName(this.getGroupName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

}
