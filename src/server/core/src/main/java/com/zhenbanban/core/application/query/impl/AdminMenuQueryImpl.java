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
package com.zhenbanban.core.application.query.impl;

import com.zhenbanban.core.application.dto.AdminMenuMetaView;
import com.zhenbanban.core.application.dto.AdminMenuView;
import com.zhenbanban.core.application.query.AdminMenuQuery;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.infrastructure.persistence.mapper.ResourcePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询实现 : 管理员菜单
 *
 * @author zhangxihai 2025/8/11
 */
@Service
@AllArgsConstructor
public class AdminMenuQueryImpl implements AdminMenuQuery {
    private final ResourcePoMapper resourcePoMapper;

    private final RedisUtils redisUtils;

    @Override
    public List<AdminMenuView> handle(Admin admin) {
        String cacheKey = CacheKeyGenerator.getAdminMenusCacheKey(admin.getId());
        if (redisUtils.hasKey(cacheKey)) return redisUtils.get(cacheKey, List.class, Collections.emptyList());

        Set<Long> roleIds = admin.getRoleIds();
        List<ResourcePo> pos = admin.isSuperAdmin() ? resourcePoMapper.findRootAll() : resourcePoMapper.findRootByIds(roleIds);

        List<AdminMenuView> list = trans(pos, "admin.menu");

        redisUtils.set(cacheKey, list, 600);

        return list;
    }

    /**
     * 转换资源列表为菜单视图列表
     *
     * @param pos        资源列表
     * @param parentName 父菜单名称
     * @return 菜单视图列表
     */
    private List<AdminMenuView> trans(List<ResourcePo> pos, String parentName) {
        List<AdminMenuView> list = new ArrayList<>(List.of());

        for (ResourcePo po : pos) {
            String resourceType = po.getResourceType();
            if (resourceType == null || resourceType.isBlank()) {
                continue;
            }
            resourceType = resourceType.toUpperCase();

            if (resourceType.equals("BUTTON")) {      // 不处理Button类型的资源
                continue;
            }

            AdminMenuView menu = new AdminMenuView();
            menu.setName(po.getResourceName());

            AdminMenuMetaView meta = new AdminMenuMetaView();
            meta.setLocale(String.format("%s.%s", parentName, po.getResourceName()));
            meta.setIcon(po.getIcon());
            meta.setOrder(po.getSort());
            meta.setHideInMenu(po.getShowInMenu() == 0);

            if (resourceType.equals("MENU") || resourceType.equals("COMPONENT")) {
                menu.setPath(po.getPath());
                menu.setComponent(po.getComponent().isBlank() ?
                        "Layout" : po.getComponent());
            } else {
                menu.setPath(po.getUrl());
                menu.setComponent("Layout");  // 非菜单或组件类型的资源，使用默认布局
            }

            if (po.getChildren() != null && po.getChildren().size() > 0) {
                meta.setButtons(getPermitButtons(po.getChildren()));
                menu.setChildren(trans(po.getChildren(), meta.getLocale()));
            }

            menu.setMeta(meta);
            list.add(menu);
        }

        return list;
    }

    /**
     * 获取按钮权限集合
     *
     * @param pos 资源列表
     * @return 按钮权限集合
     */
    private Set<String> getPermitButtons(List<ResourcePo> pos) {
        return pos.stream()
                .filter(po -> po.getResourceType().equals("BUTTON"))
                .map(ResourcePo::getResourceName)
                .collect(Collectors.toSet());
    }

}
