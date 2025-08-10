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

import com.zhenbanban.core.application.command.RoleAmdCmdHandler;
import com.zhenbanban.core.application.dto.RoleAmdCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Role;
import com.zhenbanban.core.domain.accountcontext.repository.RoleRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现: 角色增删改
 *
 * @author zhangxihai 2025/08/01
 */
@Service
@RequiredArgsConstructor
public class RoleAmdCmdHandlerImpl implements RoleAmdCmdHandler {
    private final RoleRepository roleRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(RoleAmdCommand command) {
        Long id = idGenerator.nextId();

        Role role = (new ModelMapper()).map(command, Role.class);
        role.setId(id);
        role.add();

        roleRepository.save(role, true);
        domainEventPublisher.publish(role.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(RoleAmdCommand command) {
        Role role = roleRepository.load(command.getId());
        role.checkIsAdministrator();

        role.setRoleName(command.getRoleName());
        role.setDisplayName(command.getDisplayName());
        role.setDescription(command.getDescription());
        role.modify();

        roleRepository.save(role, false);
        domainEventPublisher.publish(role.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Role role = roleRepository.load(id);
        role.checkIsAdministrator();

        if (role == null) {
            throw new BadRequestException("未找到此角色: " + id);
        }

        role.destroy();
        roleRepository.save(role, false);

        domainEventPublisher.publish(role.getEvents());
    }

}
