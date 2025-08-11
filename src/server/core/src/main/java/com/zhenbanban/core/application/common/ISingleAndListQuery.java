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
package com.zhenbanban.core.application.common;

import com.zhenbanban.core.infrastructure.support.paging.Pagination;

import java.util.List;

/**
 * Query : 通用查询接口，支持单个查询和列表查询
 *
 * @author zhangxihai 2025/8/11
 */
public interface ISingleAndListQuery<Model, Key, Command> {
    /**
     * 查询单个模型
     *
     * @param key 主键
     * @return 单个模型
     */
    Model handleQuerySingle(Key key);

    /**
     * 查询模型列表
     *
     * @param command 查询命令
     * @return 模型列表
     */
    List<Model> handleQueryList(Command command);

    /**
     * 分页查询模型列表
     *
     * @param command 查询命令
     * @param page 页码，从1开始
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Pagination<Model> handleQueryPage(Command command, long page, long pageSize);

}
