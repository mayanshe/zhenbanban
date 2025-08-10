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
package com.zhenbanban.core.domain.common;

/**
 * 领域公共：基础仓储接口
 *
 * @author zhangxihai 2025/08/01
 */
public interface IDomainRepository<Aggregate, Key> {
    /**
     * 加载聚合根
     *
     * @param id 聚合根ID
     * @return 聚合根
     */
    Aggregate load(Key id);

    /**
     * 保存聚合根
     *
     * @param aggregate 聚合根
     */
    Key save(Aggregate aggregate, boolean isNew);

}
