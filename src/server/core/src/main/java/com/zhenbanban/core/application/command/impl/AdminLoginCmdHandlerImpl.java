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

import com.zhenbanban.core.application.command.AdminLoginCmdHandler;
import com.zhenbanban.core.application.dto.AdminLoginCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import com.zhenbanban.core.domain.accountcontext.service.AccountLoginAttemptServ;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.infrastructure.util.JwtAuthUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import com.zhenbanban.core.shared.exception.UnauthorizedException;
import com.zhenbanban.core.shared.valueobj.Token;
import com.zhenbanban.core.shared.valueobj.UserClaims;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Command Handler Achieve : 管理员登陆
 *
 * @author zhangxihai 2025/8/4
 */
@Service
public class AdminLoginCmdHandlerImpl implements AdminLoginCmdHandler {
    public static final String SCOPE = "admin";

    public static final int ACCOUNT_FAILED_LIMIT = 15;

    public static final int IP_FAILED_LIMIT = 60;

    private final AdminRepository adminRepository;

    private final AccountLoginAttemptServ accountLoginAttemptServ;

    private final JwtAuthUtils<Admin> jwt;

    private final DomainEventPublisher domainEventPublisher;

    public AdminLoginCmdHandlerImpl(@Lazy AdminRepository adminRepository,
                                    @Lazy AccountLoginAttemptServ accountLoginAttemptServ,
                                    @Lazy JwtAuthUtils<Admin> jwtAuthUtils,
                                    @Lazy DomainEventPublisher domainEventPublisher) {
        this.adminRepository = adminRepository;
        this.accountLoginAttemptServ = accountLoginAttemptServ;
        this.jwt = jwtAuthUtils;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Token handle(AdminLoginCommand command) {
        // 检查登陆尝试次数
        accountLoginAttemptServ.checkLoginAttempts(
                SCOPE,
                command.getAccount(),
                ACCOUNT_FAILED_LIMIT,
                command.getClientIp(),
                IP_FAILED_LIMIT
        );

        // 通过账号加载Admin
        Admin admin = adminRepository.LoadByAccount(command.getAccount());
        if (admin == null) {
            accountLoginAttemptServ.recordFailedAttempt(
                    SCOPE,
                    command.getAccount(),
                    command.getClientIp()
            );
            throw new BadRequestException("账号或密码错误");
        }

        // 检查账号是否被删除或密码是否正确
        if (admin.isDeleted() || !admin.verifyPassword(command.getPassword())) {
            accountLoginAttemptServ.recordFailedAttempt(
                    SCOPE,
                    command.getAccount(),
                    command.getClientIp()
            );
            throw new BadRequestException("账号或密码错误");
        }

        // 生成token
        UserClaims userClaims = UserClaims.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .scope(SCOPE)
                .build();
        Token token = jwt.generate(userClaims, command.isRememberMe());

        // 发布登陆事件
        admin.login(
                SCOPE,
                "account-password",
                "boss-pc",
                command.getClientIp(),
                command.getUserAgent(),
                command.getBrowserFingerprint(),
                token.token()
        );
        domainEventPublisher.publish(admin.getEvents());

        return token;
    }

}
