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
package com.zhenbanban.bossapi.controller;

/**
 * Controller : 登陆凭证
 *
 * @author zhangxihai 2025/8/6
 */

import com.zhenbanban.bossapi.vo.AccessTokenResponse;
import com.zhenbanban.bossapi.vo.AdminLoginRequest;
import com.zhenbanban.core.application.command.AdminLoginCmdHandler;
import com.zhenbanban.core.application.command.AdminLogoutCmdHandler;
import com.zhenbanban.core.application.dto.AdminLoginCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.shared.contract.IAuth;
import com.zhenbanban.core.shared.valueobj.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccessTokenController {
    private final AdminLoginCmdHandler adminLoginCmdHandler;

    private final AdminLogoutCmdHandler adminLogoutCmdHandler;

    private IAuth<Admin> auth;

    @Autowired
    public AccessTokenController(
            @Lazy AdminLoginCmdHandler adminLoginCmdHandler,
            @Lazy AdminLogoutCmdHandler adminLogoutCmdHandler,
            @Lazy IAuth<Admin> auth
    ) {
        this.adminLoginCmdHandler = adminLoginCmdHandler;
        this.adminLogoutCmdHandler = adminLogoutCmdHandler;
        this.auth = auth;
    }

    /**
     * 创建登录凭证
     *
     * @param request            登录请求体
     * @param httpServletRequest HTTP请求对象
     * @return 登录凭证响应
     */
    @PostMapping("/access-tokens")
    public AccessTokenResponse createAccessToken(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httpServletRequest) {
        AdminLoginCommand command = AdminLoginCommand.builder()
                .account(request.getAccount())
                .password(request.getPassword())
                .rememberMe(request.isRememberMe())
                .clientIp(httpServletRequest.getRemoteAddr())
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .browserFingerprint("")           // todo : 需要实现浏览器指纹收集
                .build();
        Token token = adminLoginCmdHandler.handle(command);

        return AccessTokenResponse.builder()
                .type(token.type())
                .token(token.token())
                .expiresIn(token.expireIn())
                .timestamp(token.timestamp())
                .build();
    }

    @DeleteMapping("/access-tokens")
    @AdminPermit
    public void deleteAccessToken() {
        adminLogoutCmdHandler.handle(auth.user().getId());
    }

}
