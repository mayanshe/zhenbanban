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

import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * Mybatis Mapper 接口  : 资源
 *
 * @author zhangxihai 2025/7/11
 */
@Mapper
public interface ResourcePoMapper {
    Long insert(ResourcePo po);

    int update(ResourcePo po);

    int delete(Long id);

    int count(HashMap<String, Object> params);

    ResourcePo findById(Long id);

    Long findIdByResourceName(String resourceName);

    Long findIdByDisplayName(String displayName);

    List<ResourcePo> findByParentId(Long parentId);

    List<ResourcePo> findResourcesByRoleId(Long roleId);

    Integer findMaxSortByParentId(Long parentId);

}
