package com.zhenbanban.core.domain.institutioncontext.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HospitalLevel {
    public static final HospitalLevel LEVEL_3A = new HospitalLevel("LEVEL-3A", "三甲");
    public static final HospitalLevel LEVEL_3B = new HospitalLevel("LEVEL-3B", "三乙");
    public static final HospitalLevel LEVEL_2A = new HospitalLevel("LEVEL-2A", "二甲");
    public static final HospitalLevel LEVEL_2B = new HospitalLevel("LEVEL-2B", "二乙");
    public static final HospitalLevel LEVEL_1 = new HospitalLevel("LEVEL-1", "一级");
    public static final HospitalLevel OTHER = new HospitalLevel("OTHER", "其他");

    private static final List<HospitalLevel> ALL_LEVELS = Collections.unmodifiableList(Arrays.asList(
            LEVEL_3A,
            LEVEL_3B,
            LEVEL_2A,
            LEVEL_2B,
            LEVEL_1,
            OTHER
    ));

    private final String code;
    private final String name;

    public HospitalLevel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<HospitalLevel> all() {
        return ALL_LEVELS;
    }

    public static HospitalLevel of(String code) {
        if (code == null || code.isEmpty()) {
            return OTHER;
        }
        return ALL_LEVELS.stream()
                .filter(level -> level.getCode().equalsIgnoreCase(code))
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
        HospitalLevel that = (HospitalLevel) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "HospitalLevel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
