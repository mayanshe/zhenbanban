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
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.JwtAuthUtils;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import com.zhenbanban.core.shared.contract.IAuth;
import com.zhenbanban.core.shared.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * 配置 : 身份认证类
 *
 * @author zhangxihai 2025/8/4
 */
@Configuration
public class AuthConfig {
    @Autowired
    private JwtAuthUtils<Admin> jwt;

    @Autowired
    private AdminRepository repository;

    @Autowired
    private RedisUtils redisUtils;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public IAuth<Admin> auth() {
        String token = jwt.getToken();

        return new IAuth<Admin>() {
            private Admin admin;

            @Override
            public void verify() {
                jwt.validate();
            }

            @Override
            public Admin user() {
                verify();

                if (admin == null) {
                    long id = jwt.getUserId();
                    try {
                        String cacheKey = CacheKeyGenerator.getAuthUserKey(jwt.getScope(), id);
                        if (redisUtils.hasKey(cacheKey)) {
                            admin = redisUtils.get(cacheKey, Admin.class, null);
                        }

                        if (admin == null) {
                            admin = repository.load(id);
                            admin.setPassword("");
                            redisUtils.set(cacheKey, admin, 600);      // 缓存10分钟，避免频繁查询数据库
                        }
                    } catch (Exception e) {
                        throw new UnauthorizedException("您尚未登陆或者登陆已失效！");
                    }
                }

                return admin;
            }

            @Override
            public void logout() {
                jwt.clearToken();
            }

            @Override
            public String token() {
                return token;
            }

        };
    }

}
