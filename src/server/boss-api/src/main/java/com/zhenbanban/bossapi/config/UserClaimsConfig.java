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
package com.zhenbanban.bossapi.config;

import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.shared.contract.IAuth;
import com.zhenbanban.core.shared.valueobj.UserClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * 配置 : UserClaimsConfig
 *
 * @author zhangxihai 2025/8/4
 */
@Configuration
public class UserClaimsConfig {
    @Autowired
    private IAuth<Admin> auth;     // 注入IAuth接口，用于身份验证

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserClaims userClaims() {
        try {
            auth.verify();
            Admin admin = auth.user();
            return UserClaims.builder()
                    .id(admin.getId())
                    .username(admin.getUsername())
                    .fullname(admin.getSurname() + admin.getGivenName())
                    .scope("admin")
                    .build();
        } catch (Exception e) {
            return UserClaims.builder()
                    .id(0L)
                    .username("anonymous")
                    .fullname("未知用户")
                    .scope("unknown")
                    .build();
        }
    }

}
