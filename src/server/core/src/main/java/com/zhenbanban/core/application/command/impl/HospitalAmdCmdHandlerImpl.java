package com.zhenbanban.core.application.command.impl;

import com.zhenbanban.core.application.command.HospitalAmdCmdHandler;
import com.zhenbanban.core.application.dto.HospitalAmdCommand;
import com.zhenbanban.core.domain.institutioncontext.entity.Hospital;
import com.zhenbanban.core.domain.institutioncontext.repository.HospitalRepository;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalOwnershipType;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalStatus;
import com.zhenbanban.core.domain.institutioncontext.valueobj.HospitalType;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HospitalAmdCmdHandlerImpl implements HospitalAmdCmdHandler {

    private final HospitalRepository hospitalRepository;
    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(HospitalAmdCommand command) {
        long id = idGenerator.nextId();
        ModelMapper mapper = new ModelMapper();
        Hospital hospital = mapper.map(command, Hospital.class);
        hospital.setId(id);
        hospital.setOwnershipType(HospitalOwnershipType.of(command.getOwnershipType()));
        hospital.setHospitalType(HospitalType.of(command.getHospitalType()));
        hospital.setHospitalLevel(HospitalLevel.of(command.getHospitalLevel()));
        hospital.setStatus(HospitalStatus.of(command.getStatus()));
        hospital.add();
        return hospitalRepository.save(hospital, true);
    }

    @Override
    @Transactional
    public void handleModify(HospitalAmdCommand command) {
        Hospital hospital = hospitalRepository.load(command.getId());
        ModelMapper mapper = new ModelMapper();
        mapper.map(command, hospital);
        hospital.setOwnershipType(HospitalOwnershipType.of(command.getOwnershipType()));
        hospital.setHospitalType(HospitalType.of(command.getHospitalType()));
        hospital.setHospitalLevel(HospitalLevel.of(command.getHospitalLevel()));
        hospital.setStatus(HospitalStatus.of(command.getStatus()));
        hospital.modify();
        hospitalRepository.save(hospital, false);
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        hospitalRepository.delete(id);
    }
}
