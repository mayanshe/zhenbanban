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
package com.zhenbanban.core.domain.institutioncontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.institutioncontext.Event.HospitalActivatedEvent;
import com.zhenbanban.core.domain.institutioncontext.Event.HospitalAddedEvent;
import com.zhenbanban.core.domain.institutioncontext.Event.HospitalDestroyedEvent;
import com.zhenbanban.core.domain.institutioncontext.Event.HospitalModifiedEvent;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.common.valueobj.OwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 聚合根：医院
 *
 * @author zhangxihai 2025/08/11
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Hospital extends AbsAggregate {
    private Long id;

    private OwnershipType ownershipType;                       // 医院所有制类型（如：公立、私立、合资等）

    private HospitalType hospitalType;                        // 医院类型（如：综合医院、专科医院等）

    private HospitalLevel hospitalLevel;                      // 医院等级（如：一级、二级、三级等）

    private HospitalStatus status;                            // 医院状态（如：正常、停业、注销等）

    private String insuranceCode;                             // 医保编码（唯一标识）

    private String usccCode;                                  // 统一社会信用代码（唯一标识）

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

    /**
     * 添加医院信息
     */
    public void add() {
        this.setStatus(HospitalStatus.PENDING);

        HospitalAddedEvent event = HospitalAddedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .ownershipType(this.getOwnershipType())
                .hospitalType(this.getHospitalType())
                .hospitalLevel(this.getHospitalLevel())
                .status(this.getStatus())
                .insuranceCode(this.getInsuranceCode())
                .usccCode(this.getUsccCode())
                .hospitalCode(this.getHospitalCode())
                .hospitalName(this.getHospitalName())
                .provinceId(this.getProvinceId())
                .cityId(this.getCityId())
                .countyId(this.getCountyId())
                .address(this.getAddress())
                .postalCode(this.getPostalCode())
                .longitude(this.getLongitude())
                .latitude(this.getLatitude())
                .mapUrl(this.getMapUrl())
                .contactPhone(this.getContactPhone())
                .contactEmail(this.getContactEmail())
                .website(this.getWebsite())
                .companionDiagnosisEnabled(this.isCompanionDiagnosisEnabled())
                .mealServiceEnabled(this.isMealServiceEnabled())
                .testingDeliveryEnabled(this.isTestingDeliveryEnabled())
                .build();
        this.addEvent(event);
    }

    /**
     * 修改医院信息
     */
    public void modify() {
        if (!this.getStatus().equals(HospitalStatus.ACTIVE)) {
            throw new BadRequestException("无法修改已删除的医院信息，请先恢复");
        }

        HospitalModifiedEvent event = HospitalModifiedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .ownershipType(this.getOwnershipType())
                .hospitalType(this.getHospitalType())
                .hospitalLevel(this.getHospitalLevel())
                .status(this.getStatus())
                .insuranceCode(this.getInsuranceCode())
                .usccCode(this.getUsccCode())
                .hospitalCode(this.getHospitalCode())
                .hospitalName(this.getHospitalName())
                .provinceId(this.getProvinceId())
                .cityId(this.getCityId())
                .countyId(this.getCountyId())
                .address(this.getAddress())
                .postalCode(this.getPostalCode())
                .longitude(this.getLongitude())
                .latitude(this.getLatitude())
                .mapUrl(this.getMapUrl())
                .contactPhone(this.getContactPhone())
                .contactEmail(this.getContactEmail())
                .website(this.getWebsite())
                .companionDiagnosisEnabled(this.isCompanionDiagnosisEnabled())
                .mealServiceEnabled(this.isMealServiceEnabled())
                .testingDeliveryEnabled(this.isTestingDeliveryEnabled())
                .build();
        this.addEvent(event);
    }

    /**
     * 删除医院信息
     */

    public void destroy() {
        if (this.getStatus().equals(HospitalStatus.INACTIVE)) {
            throw new BadRequestException("医院信息已被删除，无法再次删除");
        }

        this.setStatus(HospitalStatus.INACTIVE);

        HospitalDestroyedEvent event = HospitalDestroyedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .insuranceCode(this.getInsuranceCode())
                .hospitalCode(this.getHospitalCode())
                .hospitalName(this.getHospitalName())
                .build();
        this.addEvent(event);
    }

    /**
     * 激活医院信息
     */
    public void activate() {
        if (this.getStatus().equals(HospitalStatus.ACTIVE)) {
            throw new BadRequestException("医院信息已处于激活状态，无需再次激活");
        }

        this.setStatus(HospitalStatus.ACTIVE);

        HospitalActivatedEvent event = HospitalActivatedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .insuranceCode(this.getInsuranceCode())
                .hospitalCode(this.getHospitalCode())
                .hospitalName(this.getHospitalName())
                .build();
        this.addEvent(event);
    }

}
