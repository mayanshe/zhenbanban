package com.zhenbanban.core.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("hospitals")
public class HospitalPo {
    @TableId
    private Long id;
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
    private Integer companionDiagnosisEnabled;
    private Integer mealServiceEnabled;
    private Integer testingDeliveryEnabled;
    private Long createdAt;
    private Long updatedAt;
}
