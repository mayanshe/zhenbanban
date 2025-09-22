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
package com.zhenbanban.core.domain.internethospitalcontext.entity;

import com.zhenbanban.core.domain.common.entity.AbsAggregate;
import com.zhenbanban.core.domain.internethospitalcontext.event.HospitalAddedEvent;
import com.zhenbanban.core.domain.internethospitalcontext.event.HospitalDestroyedEvent;
import com.zhenbanban.core.domain.internethospitalcontext.event.HospitalModifiedEvent;
import lombok.*;

import java.math.BigDecimal;

/**
 * 聚合根：业务医院表
 *
 * @author zhangxihai 2025/09/17
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Hospital extends AbsAggregate {

    private Long id;

    private String ownershipType;

    private String hospitalType;

    private String hospitalLevel;

    private String status;

    private String insuranceCode;

    private String usccCode;

    private String hospitalCode;

    private String hospitalName;

    private String hospitalNamePinyin;

    private String hospitalNamePinyinAbbr;

    private Long provinceId;

    private String province;

    private Long cityId;

    private String city;

    private Long countyId;

    private String county;

    private String address;

    private String postalCode;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String mapUrl;

    private String contactPhone;

    private String contactEmail;

    private String website;

    private String summary;

    private String description;

    private Boolean companionDiagnosisEnabled;

    private Boolean mealServiceEnabled;

    private Boolean testingDeliveryEnabled;

    @Builder.Default
    private boolean deleted = false;

    /**
     * 添加
     */
    public void add() {
        this.setDeleted(false);
        this.setStatus("PENDING");
        this.setTestingDeliveryEnabled(false);

        HospitalAddedEvent event = HospitalAddedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .hospitalName(this.getHospitalName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改
     */
    public void modify() {
        this.setDeleted(false);
        this.setStatus("PENDING");
        this.setTestingDeliveryEnabled(false);

        HospitalModifiedEvent event = HospitalModifiedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .hospitalName(this.getHospitalName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁
     */
    public void destroy() {
        this.setDeleted(true);

        HospitalDestroyedEvent event = HospitalDestroyedEvent.builder()
                .refId(this.getId())
                .hospitalId(this.getId())
                .hospitalName(this.getHospitalName())
                .build();

        this.addEvent(event);
    }

}
