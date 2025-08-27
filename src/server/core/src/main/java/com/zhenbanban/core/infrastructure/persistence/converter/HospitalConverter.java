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
package com.zhenbanban.core.infrastructure.persistence.converter;

import com.zhenbanban.core.domain.institutioncontext.entity.Hospital;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.common.valueobj.OwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import com.zhenbanban.core.infrastructure.persistence.po.HospitalPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * 对象映射：医院
 *
 * @author zhangxihai 2025/08/11
 */
@Mapper
public interface HospitalConverter extends IConverter {
    HospitalConverter INSTANCE = Mappers.getMapper(HospitalConverter.class);

    @Mappings({
            @Mapping(target = "ownershipType", source = "ownershipType", qualifiedByName = "ownershipTypeToString"),
            @Mapping(target = "hospitalType", source = "hospitalType", qualifiedByName = "hospitalTypeToString"),
            @Mapping(target = "hospitalLevel", source = "hospitalLevel", qualifiedByName = "hospitalLevelToString"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToString"),
            @Mapping(target = "companionDiagnosisEnabled", source = "companionDiagnosisEnabled", qualifiedByName = "booleanToShort"),
            @Mapping(target = "mealServiceEnabled", source = "mealServiceEnabled", qualifiedByName = "booleanToShort"),
            @Mapping(target = "testingDeliveryEnabled", source = "testingDeliveryEnabled", qualifiedByName = "booleanToShort")
    })
    HospitalPo toPo(Hospital hospital);

    @Mappings({
            @Mapping(target = "ownershipType", source = "ownershipType", qualifiedByName = "stringToOwnershipType"),
            @Mapping(target = "hospitalType", source = "hospitalType", qualifiedByName = "stringToHospitalType"),
            @Mapping(target = "hospitalLevel", source = "hospitalLevel", qualifiedByName = "stringToHospitalLevel"),
            @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus"),
            @Mapping(target = "companionDiagnosisEnabled", source = "companionDiagnosisEnabled", qualifiedByName = "shortToBoolean"),
            @Mapping(target = "mealServiceEnabled", source = "mealServiceEnabled", qualifiedByName = "shortToBoolean"),
            @Mapping(target = "testingDeliveryEnabled", source = "testingDeliveryEnabled", qualifiedByName = "shortToBoolean")
    })
    Hospital toAggregate(HospitalPo po);

    @Named("ownershipTypeToString")
    default String ownershipTypeToString(OwnershipType type) {
        return type.getCode();
    }

    @Named("stringToOwnershipType")
    default OwnershipType stringToOwnershipType(String code) {
        return OwnershipType.of(code);
    }

    @Named("hospitalTypeToString")
    default String hospitalTypeToString(HospitalType type) {
        return type.getCode();
    }

    @Named("stringToHospitalType")
    default HospitalType stringToHospitalType(String code) {
        return HospitalType.of(code);
    }

    @Named("hospitalLevelToString")
    default String hospitalLevelToString(HospitalLevel level) {
        return level.getCode();
    }

    @Named("stringToHospitalLevel")
    default HospitalLevel stringToHospitalLevel(String code) {
        return HospitalLevel.of(code);
    }

    @Named("statusToString")
    default String statusToString(HospitalStatus status) {
        return status.getCode();
    }

    @Named("stringToStatus")
    default HospitalStatus stringToStatus(String code) {
        return HospitalStatus.of(code);
    }

}
