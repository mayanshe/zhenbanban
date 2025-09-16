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
package com.zhenbanban.core.infrastructure.persistence.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Po : 中国行政区划
 *
 * @author zhangxihai 2025/9/16
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegionPo {
    private Long id;                  // 行政区划ID

    private Long parentId;            // 父级行政区划ID

    private String regionLevel;       // 行政区划代码

    private String postalCode;        // 邮政编码

    private String areaCode;          // 区号

    private String regionName;        // 行政区划名称

    private String namePinyin;        // 行政区划名称拼音

    private String shortName;         // 行政区划名称简称

    private String mergeName;         // 行政区组合名称

    private Double longitude;         // 经度

    private Double latitude;          // 纬度

}
