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

import com.zhenbanban.core.application.dto.RoleDto;
import com.zhenbanban.core.application.dto.RoleQuery;
import com.zhenbanban.core.application.query.RoleQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.RolePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.RolePo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Query Achieve : 角色
 *
 * @author zhangxihai 2025/8/18
 */
@Service
@RequiredArgsConstructor
public class RoleQueryHandlerImpl implements RoleQueryHandler {
    private final RolePoMapper mapper;

    @Override
    public RoleDto handleQuerySingle(Long id) {
        RolePo po = mapper.findById(id);
        if (po == null) {
            throw new BadRequestException("未找到此角色");
        }

        Set<String> permissionIds = po.getPermissionIds().stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

        Set<String> resourceIds = po.getResourceIds().stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

        return RoleDto.builder()
                .id(po.getId().toString())
                .roleName(po.getRoleName())
                .displayName(po.getDisplayName())
                .description(po.getDescription())
                .createdAt(po.getCreatedAt() == 0L ? "" : String.valueOf(po.getCreatedAt()))
                .updatedAt(po.getUpdatedAt() == 0L ? "" : String.valueOf(po.getUpdatedAt()))
                .permissionIds(permissionIds)
                .resourceIds(resourceIds)
                .build();
    }

    @Override
    public List<RoleDto> handleQueryList(RoleQuery query) {
        throw new UnsupportedOperationException("查询角色列表功能尚未实现");
    }

    @Override
    public Pagination<RoleDto> handleQueryPage(RoleQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(),
                source -> {
                    Set<String> permissionIds = source.getPermissionIds().stream()
                            .map(String::valueOf)
                            .collect(Collectors.toSet());

                    Set<String> resourceIds = source.getResourceIds().stream()
                            .map(String::valueOf)
                            .collect(Collectors.toSet());

                    return RoleDto.builder()
                            .id(source.getId().toString())
                            .roleName(source.getRoleName())
                            .displayName(source.getDisplayName())
                            .description(source.getDescription())
                            .createdAt(source.getCreatedAt() == 0L ? "" : String.valueOf(source.getCreatedAt()))
                            .updatedAt(source.getUpdatedAt() == 0L ? "" : String.valueOf(source.getUpdatedAt()))
                            .permissionIds(permissionIds)
                            .resourceIds(resourceIds)
                            .build();
                }
        );
    }

}
