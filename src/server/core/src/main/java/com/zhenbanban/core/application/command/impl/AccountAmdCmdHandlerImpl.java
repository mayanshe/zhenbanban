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

import com.zhenbanban.core.application.command.AccountAmdCmdHandler;
import com.zhenbanban.core.application.dto.AccountAmdCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Account;
import com.zhenbanban.core.domain.accountcontext.repository.AccountRepository;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command Handler Achieve : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@Service
@RequiredArgsConstructor
public class AccountAmdCmdHandlerImpl implements AccountAmdCmdHandler {
    private final AccountRepository accountRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(AccountAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper mapper = getMapper();
        Account account = mapper.map(command, Account.class);
        account.setId(id);
        account.add();

        accountRepository.save(account, true);
        domainEventPublisher.publish(account.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(AccountAmdCommand command) {
        Account account = accountRepository.load(command.getId());

        ModelMapper mapper = getMapper();
        mapper.map(command, account);
        account.modify();

        accountRepository.save(account, false);
        domainEventPublisher.publish(account.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Account account = accountRepository.load(id);
        account.destroy();

        accountRepository.save(account, false);
        domainEventPublisher.publish(account.getEvents());
    }

    @Override
    @Transactional
    public void handleActivate(Long id) {
        Account account = accountRepository.load(id);
        account.activate();

        accountRepository.save(account, false);
        domainEventPublisher.publish(account.getEvents());
    }

    private ModelMapper getMapper() {
        return new ModelMapper();
    }

}
