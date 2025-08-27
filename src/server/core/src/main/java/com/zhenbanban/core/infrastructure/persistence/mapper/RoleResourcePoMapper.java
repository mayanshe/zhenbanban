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

import com.zhenbanban.core.infrastructure.persistence.po.RoleResourcePo;

import java.util.List;
import java.util.Map;

/**
 * Mybatis Mapper Interface : 角色资源关联关系
 *
 * @author zhangxihai 2025/8/03
 */
public interface RoleResourcePoMapper {
    int insert(Map<String, Object> params);

    int deleteByRoleId(Long roleId);

    int deleteByResourceId(Long resourceId);

    List<RoleResourcePo> findResourceIdsByRoleId(Long roleId);

}
