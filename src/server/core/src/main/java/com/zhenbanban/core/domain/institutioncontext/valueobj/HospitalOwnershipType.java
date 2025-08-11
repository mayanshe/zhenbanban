package com.zhenbanban.core.domain.institutioncontext.valueobj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HospitalOwnershipType {
    public static final HospitalOwnershipType PUBLIC = new HospitalOwnershipType("PUBLIC", "公立");
    public static final HospitalOwnershipType PRIVATE = new HospitalOwnershipType("PRIVATE", "私立");
    public static final HospitalOwnershipType OTHER = new HospitalOwnershipType("OTHER", "其他");

    private static final List<HospitalOwnershipType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
            PUBLIC,
            PRIVATE,
            OTHER
    ));

    private final String code;
    private final String name;

    public HospitalOwnershipType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<HospitalOwnershipType> all() {
        return ALL_TYPES;
    }

    public static HospitalOwnershipType of(String code) {
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
        HospitalOwnershipType that = (HospitalOwnershipType) o;
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
