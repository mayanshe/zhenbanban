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

import com.zhenbanban.core.domain.common.entity.AbsAggregate;
import com.zhenbanban.core.domain.dictionarycontext.event.SyndromeAddedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.SyndromeDestroyedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.SyndromeModifiedEvent;
import lombok.*;

/**
 * 聚合根：中医证候
 *
 * @author zhangxihai 2025/09/01
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class Syndrome extends AbsAggregate {
    private Long id;
    private String syndromeCode;
    private String syndromeName;
    private String syndromeNamePinyin;
    private String syndromeNamePinyinAbbr;
    private String description;
    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加中医证候
     */
    public void add() {
        this.setDeleted(false);

        SyndromeAddedEvent event = SyndromeAddedEvent.builder()
                .refId(this.getId())
                .syndromeId(this.getId())
                .syndromeName(this.getSyndromeName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改中医证候
     */
    public void modify() {
        this.setDeleted(false);

        SyndromeModifiedEvent event = SyndromeModifiedEvent.builder()
                .refId(this.getId())
                .syndromeId(this.getId())
                .syndromeName(this.getSyndromeName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁中医证候
     */
    public void destroy() {
        this.setDeleted(true);

        SyndromeDestroyedEvent event = SyndromeDestroyedEvent.builder()
                .refId(this.getId())
                .syndromeId(this.getId())
                .syndromeName(this.getSyndromeName())
                .build();

        this.addEvent(event);
    }

}
