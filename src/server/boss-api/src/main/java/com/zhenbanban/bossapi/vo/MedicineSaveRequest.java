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
package com.zhenbanban.bossapi.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Vo : 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MedicineSaveRequest {

    @NotBlank(message = "药品编码不能为空")
    @Size(max = 24, message = "药品编码长度不能超过24个字符")
    private String medicineCode;

    @NotBlank(message = "药品商品名称不能为空")
    @Size(max = 75, message = "药品商品名称长度不能超过75个字符")
    private String medicineName;

    @NotBlank(message = "药品注册名称不能为空")
    @Size(max = 75, message = "药品注册名称长度不能超过75个字符")
    private String registeredName;

    @Builder.Default
    @Size(max = 25, message = "药品注册剂型长度不能超过25个字符")
    private String registeredMedicineModel = "";

    @Builder.Default
    @Size(max = 25, message = "药品实际剂型长度不能超过25个字符")
    private String realityMedicineModel = "";

    @Builder.Default
    @Size(max = 500, message = "药品注册规格长度不能超过500个字符")
    private String registeredOutlook = "";

    @Builder.Default
    @Size(max = 500, message = "药品实际规格长度不能超过500个字符")
    private String realityOutlook = "";

    @Builder.Default
    @Size(max = 255, message = "药品包装材质长度不能超过255个字符")
    private String materialName = "";

    @Builder.Default
    @Min(value = 0, message = "药品最小包装数量不能小于0")
    private Integer factor = 0;

    @Builder.Default
    @Size(max = 15, message = "药品最小包装单位长度不能超过15个字符")
    private String unit = "";

    @Builder.Default
    @Size(max = 15, message = "药品最小制剂单位长度不能超过15个字符")
    private String minUnit = "";

    @Builder.Default
    @Size(max = 125, message = "药品生产企业名称长度不能超过125个字符")
    private String companyName = "";

    @NotBlank(message = "药品批准文号不能为空")
    @Size(max = 100, message = "药品批准文号长度不能超过100个字符")
    private String approvalCode;

    @Builder.Default
    @Size(max = 100, message = "药品本位码长度不能超过100个字符")
    private String standardCode = "";

    @Builder.Default
    private String indication = "";

    @Builder.Default
    private String description = "";

    @Builder.Default
    private boolean otc = false;

    @Builder.Default
    private boolean poisonous = false;

}
