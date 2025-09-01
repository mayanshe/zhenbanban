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

import com.zhenbanban.core.domain.dictionarycontext.entity.Therapeutic;
import com.zhenbanban.core.infrastructure.persistence.po.TherapeuticPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 转换器: 中医治法
 *
 * @author zhangxihai 2025/09/01
 */
@Mapper
public interface TherapeuticConverter extends IConverter {
    TherapeuticConverter INSTANCE = Mappers.getMapper(TherapeuticConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "deletedAt", source = "deleted", qualifiedByName = "isDeletedToDeletedAt")
    })
    TherapeuticPo toPo(Therapeutic therapeutic);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "deletedAt", source = "deleted", qualifiedByName = "isDeletedToDeletedAt")
    })
    TherapeuticPo updatePo(Therapeutic therapeutic, @MappingTarget TherapeuticPo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true)
    })
    Therapeutic toAggregate(TherapeuticPo po);

}
