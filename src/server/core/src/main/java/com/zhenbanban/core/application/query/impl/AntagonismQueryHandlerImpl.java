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

import com.zhenbanban.core.application.dto.AntagonismDto;
import com.zhenbanban.core.application.dto.AntagonismQuery;
import com.zhenbanban.core.application.query.AntagonismQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.AntagonismPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.AntagonismPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 十八反十九畏查询处理器实现类
 *
 * @author zhangxihai 2025/09/03
 */
@Service
@RequiredArgsConstructor
public class AntagonismQueryHandlerImpl implements AntagonismQueryHandler {
    private final AntagonismPoMapper mapper;

    @Override
    public AntagonismDto handleQuerySingle(Long id) {
        AntagonismPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此十八反十九畏信息");
        }
        return (new ModelMapper()).map(po, AntagonismDto.class);
    }

    @Override
    public List<AntagonismDto> handleQueryList(AntagonismQuery query) {
        throw new UnsupportedOperationException("handleQueryList method is not implemented yet");
    }

    @Override
    public Pagination<AntagonismDto> handleQueryPage(AntagonismQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                source -> (new ModelMapper()).map(source, AntagonismDto.class));
    }
}
