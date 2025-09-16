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

import com.zhenbanban.core.application.dto.RegionDto;
import com.zhenbanban.core.application.dto.RegionQuery;
import com.zhenbanban.core.application.query.RegionListQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.RegionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.RegionPo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 中国行政区划表
 *
 * @author zhangxihai 2025/9/16
 */
@Service
@AllArgsConstructor
public class RegionListQueryHandlerImpl implements RegionListQueryHandler {
    private final RegionPoMapper regionPoMapper;

    @Override
    public List<RegionDto> handle(RegionQuery query) {
        List<RegionPo> list = regionPoMapper.findByParentId(query.getParentId());

        ModelMapper modelMapper = new ModelMapper();
        return list.stream().map(po -> modelMapper.map(po, RegionDto.class)).toList();
    }

}
