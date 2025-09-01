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
package com.zhenbanban.core.infrastructure.persistence.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Po: 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePo {

    private Long id;                            // 药品ID

    private String medicineCode;                // 药品编码

    private String medicineName;                // 药品商品名称

    private String medicineNamePinyin;          // 药品商品名称拼音

    private String medicineNamePinyinAbbr;      // 药品商品名称拼音首字母缩写

    private String registeredName;              // 药品注册名称

    private String registeredNamePinyin;        // 药品注册名称拼音

    private String registeredNamePinyinAbbr;    // 药品注册名称拼音首字母缩写

    private String registeredMedicineModel;     // 药品注册剂型

    private String realityMedicineModel;        // 药品实际剂型

    private String registeredOutlook;           // 药品注册规格

    private String realityOutlook;              // 药品实际规格

    private String materialName;                // 药品包装材质

    private Integer factor;                     // 药品最小包装数量

    private String unit;                        // 药品最小包装单位

    private String minUnit;                     // 药品最小制剂单位

    private String companyName;                 // 药品生产企业名称

    private String approvalCode;                // 药品批准文号

    private String standardCode;                // 药品本位码

    private String indication;                  // 药品适应症

    private String description;                 // 药品描述

    private Short otc;                          // 是否为非处方药 0:否 1:是

    private Short poisonous;                    // 是否为毒麻类药品 0:否 1:是

    @Builder.Default
    private long createdAt = 0L;                // 创建时间

    @Builder.Default
    private long updatedAt = 0L;                // 最后修改时间

    @Builder.Default
    private long deletedAt = 0L;                // 删除时间


}
