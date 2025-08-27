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

import com.zhenbanban.core.application.dto.ResourceDto;
import com.zhenbanban.core.application.query.ResourceQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.converter.ResourceConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.ResourcePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Query Achieve : 资源（菜单及按钮）
 *
 * @author zhangxihai 2025/8/17
 */
@Service
@RequiredArgsConstructor
public class ResourceQueryHandlerImpl implements ResourceQueryHandler {
    private final ResourcePoMapper mapper;

    @Override
    public ResourceDto handleQuerySingle(Long id) {
        ResourcePo po = mapper.findById(id);
        if (po == null) {
           throw new BadRequestException("没有找到此资源");
        }
        po.setChildren(List.of());

        return ResourceConverter.INSTANCE.toDto(po);
    }

    @Override
    public List<ResourceDto> handleQueryList() {
        Map<String, Object> condition = Map.of(
                "parentId", 0L
        );

        List<ResourcePo> pos = mapper.search(condition);

        return pos.stream()
                .map(ResourceConverter.INSTANCE::toDto)
                .toList();
    }

}
