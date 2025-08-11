package com.zhenbanban.core.domain.institutioncontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalOwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Hospital extends AbsAggregate {
    private Long id;
    private HospitalOwnershipType ownershipType;
    private HospitalType hospitalType;
    private HospitalLevel hospitalLevel;
    private HospitalStatus status;
    private String insuranceCode;
    private String hospitalCode;
    private String hospitalName;
    private Long provinceId;
    private Long cityId;
    private Long countyId;
    private String address;
    private String postalCode;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String mapUrl;
    private String contactPhone;
    private String contactEmail;
    private String website;
    private String licenseNumber;
    private boolean companionDiagnosisEnabled;
    private boolean mealServiceEnabled;
    private boolean testingDeliveryEnabled;
    private Long createdAt;
    private Long updatedAt;

    public void add() {
        // todo: add domain event
    }

    public void modify() {
        // todo: add domain event
    }

    public void destroy() {
        // todo: add domain event
    }
}
