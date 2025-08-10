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

import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.infrastructure.persistence.po.AdminPo;
import com.zhenbanban.core.infrastructure.persistence.po.RolePo;
import jdk.jfr.Name;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

/**
 * 转换器 : 管理员
 *
 * @author zhangxihai 2025/8/4
 */
@Mapper
public interface AdminConverter extends IConverter {
    AdminConverter INSTANCE = Mappers.getMapper(AdminConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "deletedAt", source = "deleted", qualifiedByName = "isDeletedToDeletedAt"),
    })
    AdminPo toPo(Admin aggregate);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "deletedAt", source = "deleted", qualifiedByName = "isDeletedToDeletedAt"),
    })
    AdminPo updatePo(Admin aggregate, @MappingTarget AdminPo adminPo);

    @Mappings({
            @Mapping(target ="roleChanged", ignore = true),
            @Mapping(target = "roleIds", source = "roles", qualifiedByName = "rolesToRoleIds"),
            @Mapping(target = "roleNames", source = "roles", qualifiedByName = "rolesToRoleNames"),
            @Mapping(target = "deleted", source = "deletedAt", qualifiedByName = "deletedAtToIsDeleted"),
    })
    Admin toAggregate(AdminPo adminPo);

    @Named("rolesToRoleIds")
    default Set<Long> mapRolesToRoleIds(Set<RolePo> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        return roles.stream()
                .map(RolePo::getId)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Named("rolesToRoleNames")
    default Set<String> mapRolesToRoleNames(Set<RolePo> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        return roles.stream()
                .map(RolePo::getRoleName)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Named("judgeIsSuperAdmin")
    default boolean judgeIsSuperAdmin(Set<RolePo> roles) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        Set<String> roleNames = roles.stream()
                .map(RolePo::getRoleName)
                .collect(java.util.stream.Collectors.toSet());

        return roleNames.contains("administrator");
    }

}
