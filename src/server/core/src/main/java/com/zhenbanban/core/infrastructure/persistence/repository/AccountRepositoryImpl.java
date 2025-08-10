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
package com.zhenbanban.core.infrastructure.persistence.repository;

import com.zhenbanban.core.domain.accountcontext.entity.Account;
import com.zhenbanban.core.domain.accountcontext.repository.AccountRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.AccountConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.AccountPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.AccountPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Domain Repository Achieve : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountPoMapper accountPoMapper;

    public AccountRepositoryImpl(
            @Lazy AccountPoMapper accountPoMapper
    ) {
        this.accountPoMapper = accountPoMapper;
    }

    @Override
    public Account load(Long id) {
        AccountPo accountPo = getPo(id);
        if (accountPo == null) {
            throw new ResourceNotFoundException("未找到此账号");
        }

        return AccountConverter.INSTANCE.toAggregate(accountPo);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Account aggregate, boolean isNew) {
        // 禁用
        if (aggregate.isForbidden()) {
            Map<String, Object> params = Map.of(
                    "id", aggregate.getId(),
                    "version", aggregate.getVersion()
            );

            if (accountPoMapper.forbid(params) <= 0) {
                throw new InternalServerException("禁用账号失败");
            }

            return aggregate.getId();
        }

        verify(aggregate);
        AccountPo po = AccountConverter.INSTANCE.toPo(aggregate);

        // 创建
        if (isNew) {
            po.setId(aggregate.getId());

            if (accountPoMapper.insert(po) <= 0) {
                throw new InternalServerException("新增账号失败");
            }

            return po.getId();
        }

        // 更新
        po.setId(aggregate.getId());

        if (accountPoMapper.update(po) <= 0) {
            throw new InternalServerException("更新账号信息失败");
        }

        return po.getId();
    }

    /**
     * 验证集
     *
     * @param aggregate 账号聚合根
     */
    private void verify(Account aggregate) {
        // 验证账号用户名是否存在
        verifyUsernameExists(aggregate);

        // 验证手机号是否存在
        verifyPhoneExists(aggregate);

        // 验证邮箱是否存在
        verifyEmailExists(aggregate);
    }

    /**
     * 验证账号用户名是否已存在
     *
     * @param aggregate 账号聚合根
     */
    private void verifyUsernameExists(Account aggregate) {
        Long exitingId = accountPoMapper.findIdByUsername(aggregate.getUsername());
        if (exitingId == null || exitingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException("账号用户名已被其他用户使用: " + aggregate.getUsername());
    }

    /**
     * 验证手机号是否已存在
     *
     * @param aggregate 账号聚合根
     */
    private void verifyPhoneExists(Account aggregate) {
        Long exitingId = accountPoMapper.findIdByPhone(aggregate.getPhone());
        if (exitingId == null || exitingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException("账号手机号已其他用户使用: " + aggregate.getPhone());
    }

    /**
     * 验证邮箱是否已存在
     *
     * @param aggregate 账号聚合根
     */
    private void verifyEmailExists(Account aggregate) {
        Long exitingId = accountPoMapper.findIdByEmail(aggregate.getEmail());
        if (exitingId == null || exitingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException("账号邮箱已被其他用户使用: " + aggregate.getEmail());
    }

    /**
     * 获取账号持久化对象
     *
     * @param id 账号ID
     * @return 账号持久化对象
     */
    private AccountPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return accountPoMapper.findById(id);
    }

}
