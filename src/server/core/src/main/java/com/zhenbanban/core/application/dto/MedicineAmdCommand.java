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
package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.application.common.BaseCommand;
import com.zhenbanban.core.infrastructure.util.PinyinUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 命令载体 : 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MedicineAmdCommand extends BaseCommand<Long> {

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
    private Integer otc;
    private Integer poisonous;

    private void setMedicineNamePinyin(String value) {
    }

    public String getMedicineNamePinyin() {
        return PinyinUtils.getPinyin(this.medicineName);
    }

    private void setMedicineNamePinyinAbbr(String value) {
    }

    public String getMedicineNamePinyinAbbr() {
        return PinyinUtils.getPinyinInitial(this.medicineName);
    }

    private void setRegisteredNamePinyin(String value) {
    }

    public String getRegisteredNamePinyin() {
        return PinyinUtils.getPinyin(this.registeredName);
    }

    private void setRegisteredNamePinyinAbbr(String value) {
    }

    public String getRegisteredNamePinyinAbbr() {
        return PinyinUtils.getPinyinInitial(this.registeredName);
    }
}
