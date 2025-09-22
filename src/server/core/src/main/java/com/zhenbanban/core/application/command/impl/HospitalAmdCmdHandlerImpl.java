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
package com.zhenbanban.core.application.command.impl;

import com.zhenbanban.core.application.command.HospitalAmdCmdHandler;
import com.zhenbanban.core.application.dto.HospitalAmdCommand;
import com.zhenbanban.core.domain.common.repository.DomainEventPublisher;
import com.zhenbanban.core.domain.internethospitalcontext.entity.Hospital;
import com.zhenbanban.core.domain.internethospitalcontext.repository.HospitalRepository;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现 : 业务医院表
 *
 * @author zhangxihai 2025/09/17
 */
@Service
@RequiredArgsConstructor
public class HospitalAmdCmdHandlerImpl implements HospitalAmdCmdHandler {
    private final HospitalRepository hospitalRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(HospitalAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper modelMapper = new ModelMapper();
        Hospital hospital = modelMapper.map(command, Hospital.class);
        hospital.setId(id);
        hospital.add();

        hospitalRepository.save(hospital, true);
        domainEventPublisher.publish(hospital.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(HospitalAmdCommand command) {
        Hospital hospital = hospitalRepository.load(command.getId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, hospital);
        hospital.modify();

        hospitalRepository.save(hospital, false);
        domainEventPublisher.publish(hospital.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Hospital hospital = hospitalRepository.load(id);
        hospital.destroy();

        hospitalRepository.save(hospital, false);
        domainEventPublisher.publish(hospital.getEvents());
    }

}
