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
package com.zhenbanban.core.domain.common.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 值对象：所有制类型
 *
 * @author zhangxihai 2025/08/11
 */
public class OwnershipType {
    public static final OwnershipType PUBLIC = new OwnershipType("PUBLIC", "公立");
    public static final OwnershipType PRIVATE = new OwnershipType("PRIVATE", "私立");
    public static final OwnershipType OTHER = new OwnershipType("OTHER", "其他");

    private static final List<OwnershipType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
            PUBLIC,
            PRIVATE,
            OTHER
    ));

    private final String code;
    private final String name;

    public OwnershipType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<OwnershipType> all() {
        return ALL_TYPES;
    }

    public static OwnershipType of(String code) {
        if (code == null || code.isEmpty()) {
            return OTHER;
        }
        return ALL_TYPES.stream()
                .filter(type -> type.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(OTHER);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnershipType that = (OwnershipType) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "HospitalOwnershipType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
