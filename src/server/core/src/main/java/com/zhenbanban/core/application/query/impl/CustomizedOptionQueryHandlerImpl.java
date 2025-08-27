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

import com.zhenbanban.core.application.dto.CustomizedOptionQuery;
import com.zhenbanban.core.application.dto.OptionDto;
import com.zhenbanban.core.application.query.CustomizedOptionQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.OptionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.OptionPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 自定义选项查询处理器实现类
 *
 * @author zhangxihai 2025/8/23
 */
@Service
@RequiredArgsConstructor
public class CustomizedOptionQueryHandlerImpl implements CustomizedOptionQueryHandler {
    private final OptionPoMapper mapper;

    @Override
    public OptionDto handleQuerySingle(Long key) {
        OptionPo po = mapper.findById(key);
        if (po == null) {
            throw new BadRequestException("没有找到此配置");
        }

        return (new ModelMapper()).map(po, OptionDto.class);
    }

    @Override
    public List<OptionDto> handleQueryList(CustomizedOptionQuery query) {
        throw new UnsupportedOperationException("不支持列表查询");
    }

    @Override
    public Pagination<OptionDto> handleQueryPage(CustomizedOptionQuery query) {
        System.out.println(query.getPage());
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(), source ->
            (new ModelMapper()).map(source, OptionDto.class));
    }

}
