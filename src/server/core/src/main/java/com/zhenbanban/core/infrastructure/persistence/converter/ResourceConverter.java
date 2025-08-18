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

import com.zhenbanban.core.application.dto.ResourceDto;
import com.zhenbanban.core.domain.accountcontext.entity.Resource;
import com.zhenbanban.core.domain.accountcontext.valueobj.ResourceType;
import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import com.zhenbanban.core.shared.exception.BadRequestException;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 转换器 : 资源
 *
 * @author zhangxihai 2025/8/03
 */
@Mapper
public interface ResourceConverter extends IConverter {
    ResourceConverter INSTANCE = Mappers.getMapper(ResourceConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "showInMenu", source = "showInMenu", qualifiedByName = "booleanToShort"),
            @Mapping(target = "resourceType", source = "resourceType", qualifiedByName = "resourceTypeToString"),
    })
    ResourcePo toPo(Resource resource);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "showInMenu", source = "showInMenu", qualifiedByName = "booleanToShort"),
            @Mapping(target = "resourceType", source = "resourceType", qualifiedByName = "resourceTypeToString"),
    })
    ResourcePo updatePo(Resource resource, @MappingTarget ResourcePo po);

    @Mappings({
            @Mapping(target = "resourceType", source = "resourceType", qualifiedByName = "stringToResourceType"),
            @Mapping(target = "showInMenu", source = "showInMenu", qualifiedByName = "shortToBoolean"),
    })
    ResourceDto toDto(ResourcePo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "showInMenu", source = "showInMenu", qualifiedByName = "shortToBoolean"),
            @Mapping(target = "resourceType", source = "resourceType", qualifiedByName = "stringToResourceType"),
            @Mapping(target = "childIds", source = "children", qualifiedByName = "childrenToChildIds"),
    })
    Resource toAggregate(ResourcePo po);

    @Named("resourceTypeToString")
    default String mapResourceTypeToString(ResourceType resourceType) {
        if (resourceType == null) {
            throw new BadRequestException("请选择资源类型");
        }

        return resourceType.getCode().toLowerCase();
    }

    @Named("stringToResourceType")
    default ResourceType mapStringToResourceType(String resourceType) {
        return ResourceType.of(resourceType.toLowerCase());
    }

    @Named("childrenToChildIds")
    default List<Long> mapChildrenToChildIds(List<ResourcePo> children) {
        if (children == null || children.isEmpty()) {
            return List.of();
        }

        return children.stream().map(ResourcePo::getId).toList();
    }

}
