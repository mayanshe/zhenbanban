package com.zhenbanban.core.infrastructure.persistence.converter;

import com.zhenbanban.core.domain.institutioncontext.entity.Hospital;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalOwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import com.zhenbanban.core.infrastructure.persistence.po.HospitalPo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HospitalConverter extends IConverter {
    HospitalConverter INSTANCE = Mappers.getMapper(HospitalConverter.class);

    @Mappings({
            @Mapping(target = "ownershipType", source = "ownershipType", qualifiedByName = "ownershipTypeToString"),
            @Mapping(target = "hospitalType", source = "hospitalType", qualifiedByName = "hospitalTypeToString"),
            @Mapping(target = "hospitalLevel", source = "hospitalLevel", qualifiedByName = "hospitalLevelToString"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToString"),
            @Mapping(target = "companionDiagnosisEnabled", source = "companionDiagnosisEnabled", qualifiedByName = "booleanToInteger"),
            @Mapping(target = "mealServiceEnabled", source = "mealServiceEnabled", qualifiedByName = "booleanToInteger"),
            @Mapping(target = "testingDeliveryEnabled", source = "testingDeliveryEnabled", qualifiedByName = "booleanToInteger")
    })
    HospitalPo toPo(Hospital hospital);

    @Mappings({
            @Mapping(target = "ownershipType", source = "ownershipType", qualifiedByName = "stringToOwnershipType"),
            @Mapping(target = "hospitalType", source = "hospitalType", qualifiedByName = "stringToHospitalType"),
            @Mapping(target = "hospitalLevel", source = "hospitalLevel", qualifiedByName = "stringToHospitalLevel"),
            @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus"),
            @Mapping(target = "companionDiagnosisEnabled", source = "companionDiagnosisEnabled", qualifiedByName = "integerToBoolean"),
            @Mapping(target = "mealServiceEnabled", source = "mealServiceEnabled", qualifiedByName = "integerToBoolean"),
            @Mapping(target = "testingDeliveryEnabled", source = "testingDeliveryEnabled", qualifiedByName = "integerToBoolean")
    })
    Hospital toAggregate(HospitalPo po);

    @Named("ownershipTypeToString")
    default String ownershipTypeToString(HospitalOwnershipType type) {
        return type.getCode();
    }

    @Named("stringToOwnershipType")
    default HospitalOwnershipType stringToOwnershipType(String code) {
        return HospitalOwnershipType.of(code);
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
