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

import com.zhenbanban.core.application.command.DepartmentAmdCmdHandler;
import com.zhenbanban.core.application.dto.DepartmentAmdCommand;
import com.zhenbanban.core.domain.internethospitalcontext.entity.Department;
import com.zhenbanban.core.domain.internethospitalcontext.repository.DepartmentRepository;
import com.zhenbanban.core.domain.common.repository.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现: 互联网医院科室
 *
 * @author zhangxihai 2025/09/16
 */
@Service
@RequiredArgsConstructor
public class DepartmentAmdCmdHandlerImpl implements DepartmentAmdCmdHandler {

    private final DepartmentRepository departmentRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(DepartmentAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper modelMapper = new ModelMapper();
        Department department = modelMapper.map(command, Department.class);
        department.setId(id);
        department.add();

        departmentRepository.save(department, true);
        domainEventPublisher.publish(department.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(DepartmentAmdCommand command) {
        Department department = departmentRepository.load(command.getId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, department);
        department.modify();

        departmentRepository.save(department, false);
        domainEventPublisher.publish(department.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Department department = departmentRepository.load(id);
        department.destroy();

        departmentRepository.save(department, false);
        domainEventPublisher.publish(department.getEvents());
    }

}
