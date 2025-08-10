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
package com.zhenbanban.core.infrastructure.persistence.mapper;

import com.zhenbanban.core.infrastructure.persistence.po.RolePermissionPo;

import java.util.List;

/**
 * Mybatis Mapper 接口 : 角色权限关联关系
 *
 * @author zhangxihai 2025/7/11
 */
public interface RolePermissionPoMapper {
    Long insert(RolePermissionPo rolePermissionPo);

    int batchInsert(List<RolePermissionPo> rolePermissionPos);

    int batchDelete(List<RolePermissionPo> rolePermissionPos);

    int deleteByRoleId(Long roleId);

    int deleteByPermissionId(Long permissionId);

    List<Long> findPermissionIdsByRoleId(Long roleId);

}
