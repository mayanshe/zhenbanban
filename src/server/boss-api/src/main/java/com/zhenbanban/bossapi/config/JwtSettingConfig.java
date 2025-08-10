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

import com.zhenbanban.core.shared.valueobj.JwtSetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类型 : JwtSetting配置
 *
 * @author zhangxihai 2025/8/4
 */
@Configuration
public class JwtSettingConfig {
    @Bean
    public JwtSetting jwtTemplate(@Value("${jwt.secret}") String secret,
                                  @Value("${jwt.expire}") long expire,
                                  @Value("${jwt.rememberExpire}") long rememberExpire,
                                  @Value("${jwt.refreshExpire}") long refreshExpire) {
        return new JwtSetting("Bearer", secret, expire, rememberExpire, refreshExpire);
    }

}
