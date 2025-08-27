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
package com.zhenbanban.core.domain.systemcontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.systemcontext.event.OptionAddedEvent;
import com.zhenbanban.core.domain.systemcontext.event.OptionDestroyedEvent;
import com.zhenbanban.core.domain.systemcontext.event.OptionModifiedEvent;
import lombok.*;

/**
 * Aggregate : 系统配置
 *
 * @author zhangxihai 2025/8/23
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Option extends AbsAggregate {
    private Long id;                          // 配置ID，唯一

    private String optionName;               // 配置名称，唯一

    private String displayName;              // 显示名称

    private String optionValue;              // 配置值

    private String description;              // 描述

    @Builder.Default
    private boolean customized = false;      // 是否为自定义配置项

    @Builder.Default
    private boolean deleted = false;        // 是否已删除

    /**
     * 创建
     */
    public void add() {
        this.setDeleted(false);
        OptionAddedEvent event = OptionAddedEvent.builder()
                .optionId(this.getId())
                .optionName(this.getOptionName())
                .displayName(this.getDisplayName())
                .optionValue(this.getOptionValue())
                .build();
        this.addEvent(event);
    }

    /**
     * 修改
     */
    public void modify() {
        this.setDeleted(false);
        OptionModifiedEvent event = OptionModifiedEvent.builder()
                .optionId(this.getId())
                .optionName(this.getOptionName())
                .displayName(this.getDisplayName())
                .optionValue(this.getOptionValue())
                .build();
        this.addEvent(event);
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (!this.isCustomized()) {
            throw new IllegalStateException("系统配置项不能被删除: " + this.optionName);
        }

        this.setDeleted(true);
        OptionDestroyedEvent event = OptionDestroyedEvent.builder()
                .optionId(this.id)
                .optionName(this.optionName)
                .build();
        this.addEvent(event);
    }

}
