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
package com.zhenbanban.core.domain.systemcontext.valueobj;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Value Object : 互联网医院配置
 *
 * @author zhangxihai 2025/9/11
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class InternetHospitalSetting implements Serializable {
    @Builder.Default
    private String hospitalName = "铁岭大铁棒子不孕不育医院";              // 医院名称

    @Builder.Default
    private String licenseNumber = "10000000000000000";                // 医疗许可证号

    @Builder.Default
    private String provinceId = "210000000000";                        // 省份ID

    @Builder.Default
    private String province = "辽宁省";                                 // 省份

    @Builder.Default
    private String cityId = "211200000000";                            // 城市ID

    @Builder.Default
    private String city = "铁岭市";                                     // 城市

    @Builder.Default
    private String countyId = "211202000000";                          // 区县ID

    @Builder.Default
    private String county = "银州区";                                   // 区县

    @Builder.Default
    private String address = "铁岭市银州区大铁棒子街道1号";                // 详细地址

    @Builder.Default
    private ArrayList<String> contactNumbers = new ArrayList<>(Arrays.asList(   // 联系电话
            "024-12345678",
            "024-87654321"
    ));

    @Builder.Default
    private ArrayList<String> serviceTimes = new ArrayList<>(Arrays.asList(     // 接诊时间
            "08:00-12:00",
            "13:00-17:00",
            "18:00-22:00"
    ));

    @Builder.Default
    private String website = "https://www.zhenbanban.com";            // 医院网址

    @Builder.Default
    private String email = "mail@zhenbanban.com";                     // 医院邮箱


    @Builder.Default
    private String introduction = """                             
            铁岭大铁棒子不孕不育医院成立于1995年，是一家集医疗、教学、科研、预防、保健为一体的现代化综合性医院。医院占地面积50000平方米，建筑面积30000平方米，设有床位300张，拥有一支高素质的医疗团队和先进的医疗设备。    
            医院设有内科、外科、妇产科、儿科、眼科、耳鼻喉科、口腔科、中医科等多个临床科室，配备了CT、MRI、超声波等先进的诊断设备。医院注重医疗质量和服务水平，致力于为患者提供优质、高效、便捷的医疗服务。        
            医院积极开展医学研究和技术创新，拥有多个省级重点实验室和研究中心。医院与多所知名医学院校合作，承担着大量的教学任务，为培养医学人才做出了重要贡献。                  
            铁岭大铁棒子不孕不育医院秉承“以患者为中心，以质量求生存，以服务求发展”的宗旨，不断提升医疗水平和服务质量，努力建设成为区域内领先的现代化综合性医院。
            """;                                                       // 医院简介

}
