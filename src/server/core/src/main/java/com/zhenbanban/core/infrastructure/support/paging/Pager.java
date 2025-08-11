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
package com.zhenbanban.core.infrastructure.support.paging;

import com.zhenbanban.core.infrastructure.persistence.mapper.PaginateMapper;

import java.util.List;
import java.util.Map;

/**
 * 类 : Pager
 *
 * @author zhangxihai 2025/8/11
 */
public final class Pager {
    private Pager() {
        throw new UnsupportedOperationException("Pager is a utility class and cannot be instantiated");
    }

    public static <M extends PaginateMapper<P>, P> Pagination<P> paginate(
            M mapper, long page, long pageSize, Map<String, Object> condition) {
        page = Math.max(page, 1);
        pageSize = Math.max(pageSize, 1);

        int total = mapper.count(condition);                                                    // 获取总记录数

        if (total == 0) {
            return new Pagination<>(page, pageSize, total, List.of());
        }

        long offset = (page - 1) * pageSize;                                                    // 计算偏移量
        List<P> items = mapper.searchByPage(condition, offset,  pageSize);                      // 分页查询

        return new Pagination<>(page, pageSize, total, items);                                  // 返回分页结果
    }

}
