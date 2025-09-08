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

import com.zhenbanban.core.domain.dictionarycontext.entity.Antagonism;
import com.zhenbanban.core.infrastructure.persistence.po.AntagonismPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 转换器 : 十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Mapper
public interface AntagonismConverter extends IConverter {
    AntagonismConverter INSTANCE = Mappers.getMapper(AntagonismConverter.class);

    AntagonismPo toPo(Antagonism antagonism);

    AntagonismPo updatePo(Antagonism antagonism, @MappingTarget AntagonismPo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true)
    })
    Antagonism toAggregate(AntagonismPo po);

}
