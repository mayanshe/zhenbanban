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
package com.zhenbanban.core.infrastructure.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 工具 : 检测当前环境
 *
 * @author zhangxihai 2025/8/03
 */
@Component
public class EnvironmentChecker {
    private final Environment environment;

    public EnvironmentChecker(Environment environment) {
        this.environment = environment;
    }

    /**
     * 是否是开发环境
     *
     * @return true if the current environment is "dev", false otherwise
     */
    public boolean isDev() {
        return environment.matchesProfiles("dev");
    }

    /**
     * 是否是测试环境
     *
     * @return true if the current environment is "test", false otherwise
     */
    public boolean isTest() {
        return environment.matchesProfiles("test");
    }

    /**
     * 是否是生产环境
     *
     * @return true if the current environment is "prod", false otherwise
     */
    public boolean isProd() {
        return environment.matchesProfiles("prod");
    }

    /**
     * 是否是开发或测试环境
     *
     * @return true if the current environment is "dev" or "test", false otherwise
     */
    public boolean isDevOrTest() {
        System.out.println( "当前环境: " + String.join(", ", environment.getActiveProfiles()));
        return environment.matchesProfiles("dev", "test");
    }

    /**
     * 是否是测试或生产环境
     *
     * @return true if the current environment is "test" or "prod", false otherwise
     */
    public boolean isTestOrProd() {
        return environment.matchesProfiles("test", "prod");
    }

}
