package com.zhenbanban.bossapi.vo;

import com.zhenbanban.core.infrastructure.support.annotation.InList;
import com.zhenbanban.core.infrastructure.support.annotation.NotBlankPattern;
import com.zhenbanban.core.infrastructure.support.annotation.NotBlankSize;
import jakarta.validation.constraints.*;
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
    @InList(message = "医院所有制类型不合法，只可以是（公立/私立/其他）", value = {"PUBLIC", "PRIVATE", "OTHER"})
    private String ownershipType;

    @NotBlank(message = "请选择医疗机构类型")
    @InList(message = "医疗机构类型不合法，只可以是（综合医院/专科医院/中医医院/民族医医院/康复医院/）", value = {"GENERAL", "SPECIALTY", "TRADITIONAL", "ETHNIC", "REHABILITATION", "OTHER"})
    private String hospitalType;

    @NotBlank(message = "请选择机构等级")
    @InList(message = "机构等级不合法，只可以是（三甲/三乙/二甲/二乙/一级/其他）", value = {"LEVEL-3A", "LEVEL-3B", "LEVEL-2A", "LEVEL-2B", "LEVEL-1", "OTHER"})
    private String hospitalLevel;

    @Builder.Default
    @NotBlankPattern(message = "医保编码格式错误（字母/数组，20个字符）", regexp = "^[A-Z0-9]{1,32}$")
    private String insuranceCode = "";

    @NotBlankPattern(message = "统一社会信用代码格式错误（字母/数组，18个字符）", regexp = "^[A-Z0-9]{1,32}$")
    private String usccCode;

    @NotBlank(message = "医疗机构登记号不能为空")
    @NotBlankPattern(message = "医疗机构登记号格式错误（字母/数组，22个字符", regexp = "^[A-Z0-9]{1,32}$")
    private String hospitalCode;

    @NotBlank(message = "医疗机构名称不能为空")
    @Size(max = 100, message = "医疗机构名称长度不能超过100个字符")
    private String hospitalName;

    @NotNull(message = "请选择医院所在省份")
    private Long provinceId;

    @Builder.Default
    private String province = "";

    @NotNull(message = "请选择医院所在城市")
    private Long cityId;

    @Builder.Default
    private String city = "";

    @NotNull(message = "请选择医院所在县区")
    private Long countyId;

    @Builder.Default
    private String county = "";

    @NotBlank(message = "医院地址不能为空")
    @Size(max = 255, message = "医院地址长度不能超过255个字符")
    private String address;

    @NotBlank(message = "邮政编码不能为空")
    @Pattern(message = "邮政编码格式错误", regexp = "^[0-9]{6}$")
    private String postalCode;

    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @NotBlank(message = "地图链接不能为空")
    @Size(max = 255, message = "地图链接长度不能超过255个字符")
    private String mapUrl;

    @Builder.Default
    @Size(max = 20, message = "医院联系电话长度不能超过20个字符")
    private String contactPhone = "";

    @Builder.Default
    @Size(max = 255, message = "医院联系邮箱长度不能超过255个字符")
    private String contactEmail = "";

    @Builder.Default
    @Size(max = 255, message = "医院官网长度不能超过255个字符")
    private String website = "";

    @Builder.Default
    @Size(max = 255, message = "医院简介长度不能超过255个字符")
    private String summary = "";

    @Builder.Default
    private String description = "";

    @Builder.Default
    private Boolean companionDiagnosisEnabled = false;

    @Builder.Default
    private Boolean mealServiceEnabled = false;

    @Builder.Default
    private Boolean testingDeliveryEnabled = false;

}
