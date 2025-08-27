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

import com.zhenbanban.core.application.command.RoleAssignmentModifyCmdHandler;
import com.zhenbanban.core.application.dto.RoleAssignmentModifyCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Role;
import com.zhenbanban.core.domain.accountcontext.repository.RoleRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Command Achieve : 角色分配修改命令处理器实现类
 *
 * @author zhangxihai 2025/7/17
 */
@Service
@RequiredArgsConstructor
public class RoleAssignmentModifyCmdHandlerImpl implements RoleAssignmentModifyCmdHandler {
    private final RoleRepository roleRepository;

    public final DomainEventPublisher domainEventPublisher;

    @Override
    public void handle(RoleAssignmentModifyCommand command) {
        Role role = roleRepository.load(command.getRoleId());
        role.modifyAssignment(command.getPermissionIds(), command.getResourceIds());

        roleRepository.modifyAssignment(role);
        domainEventPublisher.publish(role.getEvents());
    }

}
