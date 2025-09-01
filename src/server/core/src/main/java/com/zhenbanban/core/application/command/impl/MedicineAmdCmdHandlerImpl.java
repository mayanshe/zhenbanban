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

import com.zhenbanban.core.application.command.MedicineAmdCmdHandler;
import com.zhenbanban.core.application.dto.MedicineAmdCommand;
import com.zhenbanban.core.domain.dictionarycontext.entity.Medicine;
import com.zhenbanban.core.domain.dictionarycontext.repository.MedicineRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现 : 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Service
@RequiredArgsConstructor
public class MedicineAmdCmdHandlerImpl implements MedicineAmdCmdHandler {
    private final MedicineRepository medicineRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(MedicineAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper modelMapper = new ModelMapper();
        Medicine medicine = modelMapper.map(command, Medicine.class);
        medicine.setId(id);
        medicine.add();

        medicineRepository.save(medicine, true);
        domainEventPublisher.publish(medicine.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(MedicineAmdCommand command) {
        Medicine medicine = medicineRepository.load(command.getId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, medicine);
        medicine.modify();

        medicineRepository.save(medicine, false);
        domainEventPublisher.publish(medicine.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Medicine medicine = medicineRepository.load(id);
        medicine.destroy();

        medicineRepository.save(medicine, false);
        domainEventPublisher.publish(medicine.getEvents());
    }

}
