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

import com.zhenbanban.core.application.command.PermissionGroupAmdCmdHandler;
import com.zhenbanban.core.application.dto.PermissionGroupAmdCommand;
import com.zhenbanban.core.domain.accountcontext.entity.PermissionGroup;
import com.zhenbanban.core.domain.accountcontext.repository.PermissionGroupRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现 : 权限组
 *
 * @author zhangxihai 2025/8/03
 */
@Service
@RequiredArgsConstructor
public class PermissionGroupAmdCmdHandlerImpl implements PermissionGroupAmdCmdHandler {
    private final PermissionGroupRepository permissionGroupRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(PermissionGroupAmdCommand command) {
        Long id = idGenerator.nextId();

        PermissionGroup aggregate = (new ModelMapper()).map(command, PermissionGroup.class);
        aggregate.setId(id);
        aggregate.add();
        permissionGroupRepository.save(aggregate, true);
        domainEventPublisher.publish(aggregate.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(PermissionGroupAmdCommand command) {
        PermissionGroup aggregate = permissionGroupRepository.load(command.getId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, aggregate);
        aggregate.modify();

        permissionGroupRepository.save(aggregate, false);
        domainEventPublisher.publish(aggregate.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        PermissionGroup aggregate = permissionGroupRepository.load(id);
        aggregate.destroy();

        permissionGroupRepository.save(aggregate, false);
        domainEventPublisher.publish(aggregate.getEvents());
    }

}
