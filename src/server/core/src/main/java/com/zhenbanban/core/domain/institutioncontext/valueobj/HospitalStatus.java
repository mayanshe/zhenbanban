package com.zhenbanban.core.domain.institutioncontext.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HospitalStatus {
    public static final HospitalStatus PENDING = new HospitalStatus("PENDING", "待审核");
    public static final HospitalStatus ACTIVE = new HospitalStatus("ACTIVE", "启用");
    public static final HospitalStatus INACTIVE = new HospitalStatus("INACTIVE", "禁用");
    public static final HospitalStatus SUSPENDED = new HospitalStatus("SUSPENDED", "暂停");

    private static final List<HospitalStatus> ALL_STATUSES = Collections.unmodifiableList(Arrays.asList(
            PENDING,
            ACTIVE,
            INACTIVE,
            SUSPENDED
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
