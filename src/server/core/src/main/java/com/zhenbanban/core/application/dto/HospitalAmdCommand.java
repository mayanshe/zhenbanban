package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.application.common.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalAmdCommand extends BaseCommand<Long> {
    private String ownershipType;
    private String hospitalType;
    private String hospitalLevel;
    private String status;
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
}
