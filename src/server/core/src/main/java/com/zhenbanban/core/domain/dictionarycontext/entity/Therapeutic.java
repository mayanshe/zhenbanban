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
package com.zhenbanban.core.domain.dictionarycontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.dictionarycontext.event.TherapeuticAddedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.TherapeuticDestroyedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.TherapeuticModifiedEvent;
import lombok.*;

/**
 * 聚合根: 中医治法
 *
 * @author zhangxihai 2025/09/01
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class Therapeutic extends AbsAggregate {

    private Long id;
    private String therapeuticsCode;
    private String therapeuticsName;
    private String therapeuticsNamePinyin;
    private String therapeuticsNamePinyinAbbr;
    private String description;
    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加治法
     */
    public void add() {
        this.setDeleted(false);

        TherapeuticAddedEvent event = TherapeuticAddedEvent.builder()
                .refId(this.getId())
                .therapeuticId(this.getId())
                .therapeuticName(this.getTherapeuticsName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改治法
     */
    public void modify() {
        this.setDeleted(false);

        TherapeuticModifiedEvent event = TherapeuticModifiedEvent.builder()
                .refId(this.getId())
                .therapeuticId(this.getId())
                .therapeuticName(this.getTherapeuticsName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁治法
     */
    public void destroy() {
        this.setDeleted(true);

        TherapeuticDestroyedEvent event = TherapeuticDestroyedEvent.builder()
                .refId(this.getId())
                .therapeuticId(this.getId())
                .therapeuticName(this.getTherapeuticsName())
                .build();

        this.addEvent(event);
    }
}
