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
package com.zhenbanban.core.domain.internethospitalcontext.valueobj;

import java.util.List;

/**
 * Value Object : 医院所有制类型
 *
 * @author zhangxihai 2025/9/17
 */
public class HospitalOwnershipType {
    private static final HospitalOwnershipType PUBLIC = new HospitalOwnershipType("PUBLIC", "公立医院");
    private static final HospitalOwnershipType PRIVATE = new HospitalOwnershipType("PRIVATE", "私立医院");
    private static final HospitalOwnershipType OTHER = new HospitalOwnershipType("OTHER", "其他");

    private static List<HospitalOwnershipType> ALL_TYPES = List.of(
            PUBLIC,
            PRIVATE,
            OTHER
    );

    private final String code;       // 医院所有制类型代码

    private final String name;       // 医院所有制类型名称

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public HospitalOwnershipType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<HospitalOwnershipType> all() {
        return ALL_TYPES;
    }

    public static HospitalOwnershipType of(String code) {
        return ALL_TYPES.stream()
                .filter(type -> type.getCode().equals(code.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid HospitalOwnershipType code: " + code));
    }

    @Override
    public String toString() {
        return "HospitalOwnershipType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalOwnershipType that = (HospitalOwnershipType) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
}
