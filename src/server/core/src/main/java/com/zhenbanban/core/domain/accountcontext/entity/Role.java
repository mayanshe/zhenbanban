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

import com.zhenbanban.core.domain.accountcontext.event.RoleAddedEvent;
import com.zhenbanban.core.domain.accountcontext.event.RoleAssignmentModifiedEvent;
import com.zhenbanban.core.domain.accountcontext.event.RoleDestroyedEvent;
import com.zhenbanban.core.domain.accountcontext.event.RoleModifiedEvent;
import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.*;

import java.util.Set;

/**
 * 聚合根：角色
 *
 * @author zhangxihai 2025/08/01
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbsAggregate {
    private Long id;                                   // 角色ID

    private String roleName;                           // 角色名称

    private String displayName;                        // 角色显示名称

    private String description;                        // 角色描述

    @Builder.Default
    private boolean deleted = false;                   // 是否已删除

    @Builder.Default
    private Set<Long> permissionIds = Set.of();        // 角色拥有的权限ID集合

    @Builder.Default
    private Set<String> permissionNames = Set.of();    // 角色拥有的权限名称集合

    @Builder.Default
    private Set<Long> resourceIds = Set.of();          // 角色拥有的资源ID集合

    @Builder.Default
    private Set<String> resourceNames = Set.of();      // 角色拥有的资源名称集合

    /**
     * 角色名称是否为管理员
     *
     * @return true if the role is administrator, false otherwise
     */
    private boolean isAdministrator() {
        return "administrator".equals(this.roleName);
    }

    /**
     * 检查是否是管理员角色
     *
     * @throws BadRequestException if the role is administrator
     */
    public void checkIsAdministrator() {
        if (this.isAdministrator()) {
            throw new BadRequestException("不能对管理员角色进行此操作");
        }
    }

    /**
     * 添加角色
     */
    public void add() {
        this.setDeleted(false);
        this.setRoleName(this.getRoleName().toLowerCase());

        RoleAddedEvent event = RoleAddedEvent.builder()
                .refId(this.getId())
                .roleId(this.getId())
                .roleName(this.getRoleName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 修改角色
     */
    public void modify() {
        this.setDeleted(false);
        this.setRoleName(this.getRoleName().toLowerCase());

        RoleModifiedEvent event = RoleModifiedEvent.builder()
                .refId(this.getId())
                .roleId(this.getId())
                .roleName(this.getRoleName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 删除角色
     */
    public void destroy() {
        this.setDeleted(true);
        RoleDestroyedEvent event = RoleDestroyedEvent.builder()
                .refId(this.getId())
                .roleId(this.getId())
                .roleName(this.getRoleName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    public void modifyAssignment(Set<Long> permissionIds, Set<Long> resourceIds) {
        permissionIds = permissionIds == null ? Set.of() : permissionIds;
        resourceIds = resourceIds == null ? Set.of() : resourceIds;

        this.setDeleted(false);
        this.setPermissionIds(permissionIds);
        this.setResourceIds(resourceIds);

        RoleAssignmentModifiedEvent event = RoleAssignmentModifiedEvent.builder()
                .refId(this.getId())
                .roleId(this.getId())
                .permissionIds(permissionIds)
                .resourceIds(resourceIds)
                .build();
    }

}
