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
import com.zhenbanban.core.domain.dictionarycontext.event.DiagnoseAddedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.DiagnoseDestroyedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.DiagnoseModifiedEvent;
import lombok.*;

/**
 * 聚合根：疾病诊断
 *
 * @author zhangxihai 2025/08/02
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class Diagnose extends AbsAggregate {
    private Long id;
    private Integer icdType;
    private String icdCode;
    private String icdName;
    private String icdNamePinyin;
    private String icdNamePinyinAbbr;
    private String icdOptionalName;
    private String icdOptionalNamePinyin;
    private String icdOptionalNamePinyinAbbr;
    private String icdAliasName;
    private String icdAliasNamePinyin;
    private String icdAliasNamePinyinAbbr;
    private String description;
    private String chapterCode;
    private String chapterName;
    private String blockCode;
    private String blockName;
    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加疾病诊断
     */
    public void add() {
        this.setDeleted(false);

        DiagnoseAddedEvent event = DiagnoseAddedEvent.builder()
                .refId(this.getId())
                .diagnoseId(this.getId())
                .icdName(this.getIcdName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 修改疾病诊断
     */
    public void modify() {
        this.setDeleted(false);

        DiagnoseModifiedEvent event = DiagnoseModifiedEvent.builder()
                .refId(this.getId())
                .diagnoseId(this.getId())
                .icdName(this.getIcdName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 销毁疾病诊断
     */
    public void destroy() {
        this.setDeleted(true);

        DiagnoseDestroyedEvent event = DiagnoseDestroyedEvent.builder()
                .refId(this.getId())
                .diagnoseId(this.getId())
                .icdName(this.getIcdName())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }
}
