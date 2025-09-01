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

import com.zhenbanban.core.application.dto.TherapeuticDto;
import com.zhenbanban.core.application.dto.TherapeuticQuery;
import com.zhenbanban.core.application.query.TherapeuticQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.TherapeuticPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.TherapeuticPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve: 中医治法查询处理器实现类
 *
 * @author zhangxihai 2025/09/01
 */
@Service
@RequiredArgsConstructor
public class TherapeuticQueryHandlerImpl implements TherapeuticQueryHandler {
    private final TherapeuticPoMapper mapper;

    @Override
    public TherapeuticDto handleQuerySingle(Long id) {
        TherapeuticPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此中医治法");
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(po, TherapeuticDto.class);
    }

    @Override
    public List<TherapeuticDto> handleQueryList(TherapeuticQuery query) {
        throw new UnsupportedOperationException("handleQueryList method is not implemented yet");
    }

    @Override
    public Pagination<TherapeuticDto> handleQueryPage(TherapeuticQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                source -> (new ModelMapper()).map(source, TherapeuticDto.class));
    }
}
