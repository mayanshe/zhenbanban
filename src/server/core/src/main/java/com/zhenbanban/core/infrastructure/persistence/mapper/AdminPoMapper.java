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

import com.zhenbanban.core.infrastructure.persistence.po.AdminPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mybatis Mapper 接口：管理员
 *
 * @author zhangxihai 2025/08/01
 */
@Mapper
public interface AdminPoMapper {
    int insert(AdminPo rolePo);

    int update(AdminPo rolePo);

    // 禁用
    int forbid(Long id);

    // 激活
    int activate(Long id);

    AdminPo findById(Long id);

    boolean existsById(Long id);

    AdminPo findByUsername(String username);

    AdminPo findByEmail(String email);

    AdminPo findByPhone(String phone);

}
