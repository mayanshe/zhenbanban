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

import java.util.List;

/**
 * 类 : Pagination
 *
 * @author zhangxihai 2025/8/11
 */
public class Pagination<T> {
    private long total = 0;                // 总记录数

    private long pageSize = 15;            // 每页记录数

    private long totalPage = 0;            // 总页数

    private long page = 1;                 // 当前页码

    private List<T> items = List.of();     // 当前页数据列表，默认为空列表

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        this.total = total;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("PageSize must be greater than 0");
        }
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        if (totalPage == 0 && total > 0) {
            totalPage = (total + pageSize - 1) / pageSize; // 计算总页数
        }
        return totalPage;
    }

    private void setTotalPage(long totalPage) {
        throw new UnsupportedOperationException("TotalPage cannot be set directly");
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page <= 0 ? 1 : page; // 确保页码至少为1
    }

    public long getCount() {
        return items.size();
    }

    private void setCount(long count) {
        throw new UnsupportedOperationException("Count cannot be set directly");
    }

    public long getPrevPage() {
        return page > 0 ? page - 1 : 1;
    }

    private void setPrevPage(long prevPage) {
        throw new UnsupportedOperationException("PrevPage cannot be set directly");
    }

    public long getNextPage() {
        return page < getTotalPage() ? page + 1 : 0;
    }

    private void setNextPage(long nextPage) {
        throw new UnsupportedOperationException("NextPage cannot be set directly");
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        if (items == null) {
            items = List.of(); // 确保items不为null
        }

        this.items = items;
    }

    public Pagination() {}

    public Pagination(long total, long pageSize, long page, List<T> items) {
        setTotal(total);
        setPageSize(pageSize);
        setPage(page);
        setItems(items);
    }

}
