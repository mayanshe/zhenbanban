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

import com.zhenbanban.core.application.command.AdminAmdCmdHandler;
import com.zhenbanban.core.application.dto.AdminAmdCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.domain.accountcontext.repository.AccountRepository;
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command Handler Achieve : 管理员
 *
 * @author zhangxihai 2025/8/4
 */
@Service
@RequiredArgsConstructor
public class AdminAmdCmdHandlerImpl implements AdminAmdCmdHandler {
    private final AdminRepository adminRepository;

    private final DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public Long handleAdd(AdminAmdCommand command) {
        ModelMapper mapper = getMapper();
        Admin admin = mapper.map(command, Admin.class);
        admin.add();

        Long id = adminRepository.save(admin, true);
        domainEventPublisher.publish(admin.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(AdminAmdCommand command) {
        Admin admin = adminRepository.load(command.getId());

        ModelMapper mapper = getMapper();
        mapper.map(command, admin);
        admin.modify();

        adminRepository.save(admin, false);
        domainEventPublisher.publish(admin.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Admin admin = adminRepository.load(id);
        admin.destroy();

        adminRepository.save(admin, false);
        domainEventPublisher.publish(admin.getEvents());
    }

    @Override
    public void handleActivate(Long id) {
        Admin admin = adminRepository.load(id);
        admin.activate();

        adminRepository.save(admin, false);
        domainEventPublisher.publish(admin.getEvents());
    }

    private ModelMapper getMapper() {
        return new ModelMapper();
    }

}
