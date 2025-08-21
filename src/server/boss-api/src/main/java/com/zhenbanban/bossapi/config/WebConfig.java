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

import com.zhenbanban.core.infrastructure.support.annotation.AdminPermitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 : WebConfig
 *
 * @author zhangxihai 2025/8/20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AdminPermitInterceptor adminPermitInterceptor;

    public WebConfig(AdminPermitInterceptor adminPermitInterceptor) {
        this.adminPermitInterceptor = adminPermitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPermitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/access-tokens", "/assets/**"
                );
    }

}
