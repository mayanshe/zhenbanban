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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 领域公共: 抽象基础聚合根类
 *
 * @author zhangxihai 2025/08/01
 */
public abstract class AbsAggregate {
    private final List<AbsDomainEvent> events = new ArrayList<>();

    /**
     * 注册领域事件
     *
     * @param event 领域事件
     */
    public void addEvent(AbsDomainEvent event) {
        if (event != null) {
            events.add(event);
        }
    }

    /**
     * 获取领域事件列表
     *
     * @return 领域事件列表
     */
    public List<AbsDomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * 清空领域事件列表
     */
    public void clearEvents() {
        events.clear();
    }

    abstract public Long getId();

}
