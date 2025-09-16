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

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request : 互联网医院配置修改
 *
 * @author zhangxihai 2025/9/11
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class InternetHospitalSettingModifyRequest {
    @NotBlank(message = "互联网医院名称不能为空")
    private String hospitalName;                                   // 医院名称

    @NotBlank(message = "互联网医院医疗许可证号不能为空")
    private String licenseNumber;                                 // 医疗许可证号

    @NotBlank(message = "互联网医院所在省份不能为空")
    private String provinceId;                                    // 省份ID

    @NotBlank(message = "互联网医院所在省份不能为空")
    private String province;                                      // 省份

    @NotBlank(message = "互联网医院所在城市不能为空")
    private String cityId;                                        // 区县ID

    @NotBlank(message = "互联网医院所在城市不能为空")
    private String city;                                          // 城市

    @NotBlank(message = "互联网医院所在区县不能为空")
    private String countyId;                                      // 区县ID

    @NotBlank(message = "互联网医院所在区县不能为空")
    private String county;                                        // 区县

    @NotBlank(message = "互联网医院详细地址不能为空")
    private String address;                                       // 详细地址

    private String contactNumbers;                                // 联系电话

    @NotBlank(message = "互联网医院接诊时间不能为空")
    private String serviceTimes;                                 // 接诊时间

    @NotBlank(message = "互联网医院医院网址不能为空")
    private String website;                                      // 医院网址

    @NotBlank(message = "互联网医院医院邮箱不能为空")
    private String email;                                        // 医院邮箱

    @NotBlank(message = "互联网医院医院简介不能为空")                // 医院简介
    private String introduction;

}
