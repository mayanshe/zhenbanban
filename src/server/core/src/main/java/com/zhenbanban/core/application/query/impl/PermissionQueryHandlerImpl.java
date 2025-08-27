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

import com.zhenbanban.core.application.dto.PermissionDto;
import com.zhenbanban.core.application.dto.PermissionQuery;
import com.zhenbanban.core.application.query.PermissionQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Query Achieve : 权限查询处理器实现类
 *
 * @author zhangxihai 2025/8/15
 */
@Service
@RequiredArgsConstructor
public class PermissionQueryHandlerImpl implements PermissionQueryHandler {
    private final PermissionPoMapper mapper;

    @Override
    public PermissionDto handleQuerySingle(Long id){
        PermissionPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此权限");
        }

        return (new ModelMapper()).map(po, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> handleQueryList(PermissionQuery query) {
        throw new UnsupportedOperationException("handleQueryList method is not implemented yet");
    }

    @Override
    public Pagination<PermissionDto> handleQueryPage(PermissionQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                source -> (new ModelMapper()).map(source, PermissionDto.class));
    }

}
