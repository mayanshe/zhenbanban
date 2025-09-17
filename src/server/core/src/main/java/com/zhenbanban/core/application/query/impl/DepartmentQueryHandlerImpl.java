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

import com.zhenbanban.core.application.dto.DepartmentDto;
import com.zhenbanban.core.application.dto.DepartmentQuery;
import com.zhenbanban.core.application.query.DepartmentQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.converter.DepartmentConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.DepartmentPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.DepartmentPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Query Achieve: 互联网医院科室查询处理器实现类
 *
 * @author zhangxihai 2025/09/16
 */
@Service
@RequiredArgsConstructor
public class DepartmentQueryHandlerImpl implements DepartmentQueryHandler {

    private final DepartmentPoMapper mapper;

    @Override
    public DepartmentDto handleQuerySingle(Long id) {
        DepartmentPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此科室");
        }
        return DepartmentConverter.INSTANCE.toDto(po);
    }

    @Override
    public List<DepartmentDto> handleQueryList(DepartmentQuery query) {
        List<DepartmentPo> poList = mapper.search(query.toMap());

        return poList.stream()
                .map(DepartmentConverter.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<DepartmentDto> handleQueryPage(DepartmentQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                source -> {
                    return DepartmentConverter.INSTANCE.toDto( (DepartmentPo) source);
                });
    }

}
