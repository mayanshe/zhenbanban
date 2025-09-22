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

import com.zhenbanban.core.application.dto.HospitalDto;
import com.zhenbanban.core.application.dto.HospitalQuery;
import com.zhenbanban.core.application.query.HospitalQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.converter.HospitalConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.HospitalPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.HospitalPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 业务医院表查询处理器实现类
 *
 * @author zhangxihai 2025/09/17
 */
@Service
@RequiredArgsConstructor
public class HospitalQueryHandlerImpl implements HospitalQueryHandler {
    private final HospitalPoMapper mapper;

    @Override
    public HospitalDto handleQuerySingle(Long id) {
        HospitalPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此业务医院表");
        }

        return HospitalConverter.INSTANCE.toDto(po);
    }

    @Override
    public List<HospitalDto> handleQueryList(HospitalQuery query) {
        throw new UnsupportedOperationException("handleQueryList method is not implemented yet");
    }

    @Override
    public Pagination<HospitalDto> handleQueryPage(HospitalQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                HospitalConverter.INSTANCE::toDto);
    }

}
