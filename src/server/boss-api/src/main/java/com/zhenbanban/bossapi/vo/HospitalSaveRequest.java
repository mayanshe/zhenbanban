package com.zhenbanban.bossapi.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalSaveRequest {
    @NotBlank(message = "请选择医院所有制类型")
    private String ownershipType;
    @NotBlank(message = "请选择医疗机构类型")
    private String hospitalType;
    @NotBlank(message = "请选择机构等级")
    private String hospitalLevel;
    @NotBlank(message = "请选择医院状态")
    private String status;
    private String insuranceCode;
    @NotBlank(message = "医疗机构登记号不能为空")
    private String hospitalCode;
    @NotBlank(message = "医疗机构名称不能为空")
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
