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
package com.zhenbanban.core.domain.institutioncontext.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 值对象：医院状态
 *
 * @author zhangxihai 2025/08/11
 */
public class HospitalStatus {
    public static final HospitalStatus PENDING = new HospitalStatus("PENDING", "待审核");
    public static final HospitalStatus ACTIVE = new HospitalStatus("ACTIVE", "启用");
    public static final HospitalStatus INACTIVE = new HospitalStatus("INACTIVE", "禁用");

    private static final List<HospitalStatus> ALL_STATUSES = Collections.unmodifiableList(Arrays.asList(
            PENDING,
            ACTIVE,
            INACTIVE
    ));

    private final String code;
    private final String name;

    public HospitalStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<HospitalStatus> all() {
        return ALL_STATUSES;
    }

    public static HospitalStatus of(String code) {
        if (code == null || code.isEmpty()) {
            return PENDING;
        }
        return ALL_STATUSES.stream()
                .filter(status -> status.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(PENDING);
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
        HospitalStatus that = (HospitalStatus) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "HospitalStatus{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
