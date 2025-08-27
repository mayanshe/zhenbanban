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

import com.zhenbanban.core.application.dto.PermissionGroupDto;
import com.zhenbanban.core.application.query.PermissionGroupQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionGroupPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionGroupPo;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 权限组查询实现类
 *
 * @author zhangxihai 2025/8/14
 */
@Service
@RequiredArgsConstructor
public class PermissionGroupQueryHandlerImpl implements PermissionGroupQueryHandler {
    private final PermissionGroupPoMapper mapper;

    @Override
    public PermissionGroupDto handleQuerySingle(Long id) {
        PermissionGroupPo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("没有找到此权限组");
        }
        po.setChildren(null);
        return (new ModelMapper()).map(po, PermissionGroupDto.class);
    }

    @Override
    public List<PermissionGroupDto> handleQueryRootList() {
        List<PermissionGroupPo> pos = mapper.findRootAll();
        return pos.stream()
                .map(po -> (new ModelMapper()).map(po, PermissionGroupDto.class))
                .toList();
    }

}
