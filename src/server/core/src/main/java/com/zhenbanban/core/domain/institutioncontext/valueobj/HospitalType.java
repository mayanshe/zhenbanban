package com.zhenbanban.core.domain.institutioncontext.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HospitalType {
    public static final HospitalType GENERAL = new HospitalType("GENERAL", "综合医院");
    public static final HospitalType SPECIALTY = new HospitalType("SPECIALTY", "专科医院");
    public static final HospitalType TRADITIONAL = new HospitalType("TRADITIONAL", "中医医院");
    public static final HospitalType ETHNIC = new HospitalType("ETHNIC", "民族医医院");
    public static final HospitalType REHABILITATION = new HospitalType("REHABILITATION", "康复医院");
    public static final HospitalType OTHER = new HospitalType("OTHER", "其他");

    private static final List<HospitalType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
            GENERAL,
            SPECIALTY,
            TRADITIONAL,
            ETHNIC,
            REHABILITATION,
            OTHER
    ));

    private final String code;
    private final String name;

    public HospitalType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<HospitalType> all() {
        return ALL_TYPES;
    }

    public static HospitalType of(String code) {
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
        HospitalType that = (HospitalType) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "HospitalType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
