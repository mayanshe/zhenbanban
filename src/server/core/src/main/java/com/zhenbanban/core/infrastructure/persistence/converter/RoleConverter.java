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
package com.zhenbanban.core.infrastructure.persistence.converter;

import com.zhenbanban.core.domain.accountcontext.entity.Role;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionPo;
import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import com.zhenbanban.core.infrastructure.persistence.po.RolePo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 转换器: 角色
 *
 * @author zhangxihai 2025/08/01
 */

@Mapper
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "resources", ignore = true),
    })
    RolePo toPo(Role role);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "resources", ignore = true),
    })
    RolePo updatePo(Role role, @MappingTarget RolePo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "permissionIds", source = "permissions", qualifiedByName = "permissionToPermissionIds"),
            @Mapping(target = "permissionNames", source = "permissions", qualifiedByName = "permissionToPermissionNames"),
            @Mapping(target = "resourceIds", source = "resources", qualifiedByName = "resourceToResourceIds"),
            @Mapping(target = "resourceNames", source = "resources", qualifiedByName = "resourceToResourceNames"),
    })
    Role toAggregate(RolePo po);

    @Named("permissionToPermissionIds")
    default Set<Long> mapPermissionToPermissionIds(List<PermissionPo> permissions) {
        return permissions.stream()
                .map(PermissionPo::getId)
                .collect(Collectors.toSet());
    }

    @Named("permissionToPermissionNames")
    default Set<String> mapPermissionToPermissionNames(List<PermissionPo> permissions) {
        return permissions.stream()
                .map(PermissionPo::getPermissionName)
                .collect(Collectors.toSet());
    }

    @Named("resourceToResourceIds")
    default Set<Long> mapResourceToResourceIds(List<ResourcePo> resources) {
        return resources.stream()
                .map(ResourcePo::getId)
                .collect(Collectors.toSet());
    }

    @Named("resourceToResourceNames")
    default Set<String> mapResourceToResourceNames(List<ResourcePo> resources) {
        return resources.stream()
                .map(ResourcePo::getResourceName)
                .collect(Collectors.toSet());
    }

}
