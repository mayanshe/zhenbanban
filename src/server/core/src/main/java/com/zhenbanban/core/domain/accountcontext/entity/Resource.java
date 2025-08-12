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

import com.zhenbanban.core.domain.accountcontext.event.ResourceAddedEvent;
import com.zhenbanban.core.domain.accountcontext.event.ResourceDestroyedEvent;
import com.zhenbanban.core.domain.accountcontext.event.ResourceModifiedEvent;
import com.zhenbanban.core.domain.accountcontext.valueobj.ResourceType;
import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import lombok.*;

import java.util.List;

/**
 * Aggregate : 资源
 *
 * @author zhangxihai 2025/8/03
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Resource extends AbsAggregate {
    private Long id;                                   // 资源ID

    private Long parentId;                             // 父资源ID

    private ResourceType resourceType;                 // 资源类型（0: 菜单, 1: 按钮, 2: 页面）

    private String resourceName;                       // 资源名称

    private String displayName;                        // 资源显示名称

    @Builder.Default
    private String description = "";                    // 资源描述

    @Builder.Default
    private String path = "";                          // 资源路径（用于路由）

    @Builder.Default
    private String url = "";                           // 资源URL

    @Builder.Default
    private String icon = "";                          // 资源图标

    @Builder.Default
    private String component = "";                     // 资源组件路径

    @Builder.Default
    private int sort = 0;                              // 资源排序，数字越小越靠前

    @Builder.Default
    private boolean showInMenu = false;                // 是否在菜单中显示（0: 不显示, 1: 显示）

    @Builder.Default
    private boolean deleted = false;                   // 是否已删除

    @Builder.Default
    private List<Long> childIds = List.of();           // 子资源ID列表

    /**
     * 添加资源
     */
    public void add() {
        this.setDeleted(false);
        this.verify();

        ResourceAddedEvent event = ResourceAddedEvent.builder()
                .refId(this.getId())
                .resourceId(this.getId())
                .parentId(this.getParentId())
                .resourceType(this.getResourceType())
                .resourceName(this.getResourceName())
                .url(this.getUrl())
                .icon(this.getIcon())
                .component(this.getComponent())
                .showInMenu(this.isShowInMenu())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 修改资源
     */
    public void modify() {
        this.setDeleted(false);
        this.verify();

        ResourceModifiedEvent event = ResourceModifiedEvent.builder()
                .refId(this.getId())
                .resourceId(this.getId())
                .parentId(this.getParentId())
                .resourceType(this.getResourceType())
                .resourceName(this.getResourceName())
                .url(this.getUrl())
                .icon(this.getIcon())
                .component(this.getComponent())
                .showInMenu(this.isShowInMenu())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 删除资源
     */
    public void destroy() {
        PrintUtils.toConsole(this.childIds);
        if (this.childIds != null && !this.childIds.isEmpty()) {
            throw new IllegalStateException("无法删除资源，存在子资源，请先删除子资源");
        }

        this.setDeleted(true);

        ResourceDestroyedEvent event = ResourceDestroyedEvent.builder()
                .refId(this.getId())
                .resourceId(this.getId())
                .parentId(this.getParentId())
                .resourceType(this.getResourceType())
                .resourceName(this.getResourceName())
                .url(this.getUrl())
                .icon(this.getIcon())
                .component(this.getComponent())
                .showInMenu(this.isShowInMenu())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 验证资源的完整性和一致性
     */
    private void verify() {
        if (this.getResourceType().equals(ResourceType.MENU)) {
            if (this.getPath() == null || this.getPath().isEmpty()) {
                throw new IllegalArgumentException("菜单资源必须指定路径");
            }

            this.setUrl("");

            return;
        }

        if (this.getResourceType().equals(ResourceType.COMPONENT)) {
            if (this.getPath() == null || this.getPath().isEmpty()) {
                throw new IllegalArgumentException("菜单资源必须指定路径");
            }

            if (this.getComponent() == null || this.getComponent().isEmpty()) {
                throw new IllegalArgumentException("组件资源必须指定组件路径");
            }

            this.setUrl("");

            return;
        }

        if (this.getResourceType().equals(ResourceType.LINK)) {
            if (this.getUrl() == null || this.getUrl().isEmpty()) {
                throw new IllegalArgumentException("链接资源必须指定URL");
            }
            this.setPath("");
            this.setComponent("");

            return;
        }

        if (this.getResourceType().equals(ResourceType.BUTTON)) {
            this.setPath("");
            this.setComponent("");
            this.setUrl("");
            this.setShowInMenu(false);

            return;
        }
    }


}
