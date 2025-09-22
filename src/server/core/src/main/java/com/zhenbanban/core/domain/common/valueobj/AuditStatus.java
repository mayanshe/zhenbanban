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

import java.util.List;

/**
 * Value Object : 审核状态
 *
 * @author zhangxihai 2025/9/17
 */
public class AuditStatus {
    private static final AuditStatus PENDING = new AuditStatus("PENDING", "待审");
    private static final AuditStatus APPROVED = new AuditStatus("APPROVED", "通过");
    private static final AuditStatus REJECTED = new AuditStatus("REJECTED", "拒绝");

    private static final java.util.List<AuditStatus> ALL_STATUSES = java.util.List.of(
            PENDING,
            APPROVED,
            REJECTED
    );

    private final String code;       // 审核状态代码

    private final String name;       // 审核状态名称

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public AuditStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<AuditStatus> all() {
        return ALL_STATUSES;
    }

    public static AuditStatus of(String code) {
        return ALL_STATUSES.stream()
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid AuditStatus code: " + code));
    }

    @Override
    public String toString() {
        return "AuditStatus{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditStatus that = (AuditStatus) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

}
