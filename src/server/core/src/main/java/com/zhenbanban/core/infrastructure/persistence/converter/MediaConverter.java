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

import com.zhenbanban.core.domain.systemcontext.entity.Media;
import com.zhenbanban.core.infrastructure.persistence.po.MediaPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Converter : 媒体资源转换器
 *
 * @author zhangxihai 2025/8/26
 */
@Mapper
public interface MediaConverter {
    MediaConverter INSTANCE = Mappers.getMapper(MediaConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    MediaPo toPo(Media aggregate);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    MediaPo updatePo(Media aggregate, @MappingTarget MediaPo po);

    @Mappings({
            @Mapping(target = "deleted", ignore = true)
    })
    Media toAggregate(MediaPo po);

}
