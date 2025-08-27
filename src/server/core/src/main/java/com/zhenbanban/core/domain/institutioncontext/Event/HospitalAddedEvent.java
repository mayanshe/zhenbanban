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
package com.zhenbanban.core.domain.institutioncontext.Event;

import com.zhenbanban.core.domain.common.AbsDomainEvent;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.common.valueobj.OwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 事件 : 医院添加成功事件
 *
 * @author zhangxihai 2025/8/11
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalAddedEvent extends AbsDomainEvent {
    private Long hospitalId;                                  // 医院ID

    private OwnershipType ownershipType;              // 医院所有制类型（如：公立、私立、合资等）

    private HospitalType hospitalType;                        // 医院类型（如：综合医院、专科医院等）

    private HospitalLevel hospitalLevel;                      // 医院等级（如：一级、二级、三级等）

    private HospitalStatus status;                            // 医院状态（如：正常、停业、注销等）

    private String insuranceCode;                             // 医保编码（唯一标识）

    private String usccCode;                                   // 统一社会信用代码（唯一标识）

    private String hospitalCode;                              // 医院编码（唯一标识）

    private String hospitalName;                              // 医院名称

    private Long provinceId;                                  // 省份ID

    private Long cityId;                                      // 城市ID

    private Long countyId;                                    // 区县ID

    private String address;                                   // 医院地址

    private String postalCode;                                // 邮政编码

    private BigDecimal longitude;                             // 经度和纬度，用于地图定位

    private BigDecimal latitude;                              // 经度和纬度，用于地图定位

    private String mapUrl;                                    // 地图链接

    private String contactPhone;                              // 联系电话

    private String contactEmail;                              // 联系邮箱

    private String website;                                   // 医院官网地址

    private boolean companionDiagnosisEnabled;                // 是否支持陪诊

    private boolean mealServiceEnabled;                       // 是否支持餐饮服务

    private boolean testingDeliveryEnabled;                   // 是否支持检测送检服务

    private Long createdAt;                                   // 创建时间

    private Long updatedAt;                                   // 更新时间

}
