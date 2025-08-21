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

import com.zhenbanban.core.application.dto.GroupedPermissionDto;
import com.zhenbanban.core.application.query.GroupedPermissionQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionGroupPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionGroupPo;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.modelmapper.TypeToken;

import java.util.List;

/**
 * Query Achieve : 分组权限查询处理器实现类
 *
 * @author zhangxihai 2025/8/19
 */
@Service
@RequiredArgsConstructor
public class GroupedPermissionQueryHandlerImpl implements GroupedPermissionQueryHandler {

    private final PermissionGroupPoMapper permissionGroupPoMapper;

    @Override
    public List<GroupedPermissionDto> handle() {
        List<PermissionGroupPo> pos = permissionGroupPoMapper.findRootAll();
        return (new ModelMapper()).map(pos, new TypeToken<List<GroupedPermissionDto>>(){}.getType());
    }

}
