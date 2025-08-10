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

import com.zhenbanban.core.application.command.AdminRolesBindingCmdHandler;
import com.zhenbanban.core.application.dto.AdminRolesBindingCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Command Handler Achieve : 管理员角色绑定
 *
 * @author zhangxihai 2025/8/5
 */
@Service
@RequiredArgsConstructor
public class AdminRolesBindingCmdHandlerImpl implements AdminRolesBindingCmdHandler {
    private final AdminRepository adminRepository;

    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void handle(AdminRolesBindingCommand command) {
        Admin admin = adminRepository.load(command.getId());
        admin.bindingRoles(command.getRoleIds());

        adminRepository.save(admin, false);

        // todo : 清楚admin的用户信息缓存

        domainEventPublisher.publish(admin.getEvents());
    }

}
