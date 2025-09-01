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
import com.zhenbanban.core.domain.dictionarycontext.event.MedicineAddedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.MedicineDestroyedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.MedicineModifiedEvent;
import lombok.*;

/**
 * 聚合根：西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Medicine extends AbsAggregate {
    private Long id;
    private String medicineCode;
    private String medicineName;
    private String medicineNamePinyin;
    private String medicineNamePinyinAbbr;
    private String registeredName;
    private String registeredNamePinyin;
    private String registeredNamePinyinAbbr;
    private String registeredMedicineModel;
    private String realityMedicineModel;
    private String registeredOutlook;
    private String realityOutlook;
    private String materialName;
    private Integer factor;
    private String unit;
    private String minUnit;
    private String companyName;
    private String approvalCode;
    private String standardCode;
    private String indication;
    private String description;
    private Boolean otc;
    private Boolean poisonous;
    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加
     */
    public void add() {
        this.setDeleted(false);

        MedicineAddedEvent event = MedicineAddedEvent.builder()
                .refId(this.getId())
                .medicineId(this.getId())
                .medicineName(this.getMedicineName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改
     */
    public void modify() {
        this.setDeleted(false);

        MedicineModifiedEvent event = MedicineModifiedEvent.builder()
                .refId(this.getId())
                .medicineId(this.getId())
                .medicineName(this.getMedicineName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁
     */
    public void destroy() {
        this.setDeleted(true);

        MedicineDestroyedEvent event = MedicineDestroyedEvent.builder()
                .refId(this.getId())
                .medicineId(this.getId())
                .medicineName(this.getMedicineName())
                .build();

        this.addEvent(event);
    }
}
