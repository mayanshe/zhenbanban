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
package com.zhenbanban.core.domain.dictionarycontext.valueobj;

import java.util.List;

/**
 * Value Object : 互联网医院科室类型
 *
 * @author zhangxihai 2025/9/16
 */
public class DepartmentType {
    private static final DepartmentType CLINICAL = new DepartmentType("clinical", "临床科室");
    private static final DepartmentType TECHNOLOGY = new DepartmentType("technology", "技术科室");
    private static final DepartmentType EMERGENCY = new DepartmentType("emergency", "急诊与重症科室");
    private static final DepartmentType LOGISTICS = new DepartmentType("logistics", "医技科室");

    private static final List<DepartmentType> ALL_TYPES = List.of(
            CLINICAL,
            TECHNOLOGY,
            EMERGENCY,
            LOGISTICS
    );

    private final String code;       // 科室类型代码

    private final String name;       // 科室类型名称

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public DepartmentType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<DepartmentType> all() {
        return ALL_TYPES;
    }

    public static DepartmentType of(String code) {
        return ALL_TYPES.stream()
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid DepartmentType code: " + code));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentType that = (DepartmentType) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "DepartmentType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
