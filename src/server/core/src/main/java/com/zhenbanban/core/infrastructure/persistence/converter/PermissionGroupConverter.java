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

import com.zhenbanban.core.domain.accountcontext.entity.PermissionGroup;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionGroupPo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换器 : PermissionGroupConverter
 *
 * @author zhangxihai 2025/8/03
 */
@Mapper
public interface PermissionGroupConverter {
    PermissionGroupConverter INSTANCE = Mappers.getMapper(PermissionGroupConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "children", ignore = true)
    })
    PermissionGroupPo toPo(PermissionGroup permissionGroup);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "children", ignore = true)
    })
    PermissionGroupPo updatePo(PermissionGroup permissionGroup, @MappingTarget PermissionGroupPo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(source = "children", target = "childrenIds", qualifiedByName = "childrenToChildrenIds")
    })
    PermissionGroup toAggregate(PermissionGroupPo po);

    @Named("childrenToChildrenIds")
    default List<Long> mapChildrenToChildrenIds(List<PermissionGroupPo> children) {
        return children.stream().map(PermissionGroupPo::getId).collect(Collectors.toList());
    }

}
