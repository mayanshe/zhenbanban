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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Util : 缓存键生成
 *
 * @author zhangxihai 2025/8/4
 */
public final class CacheKeyGenerator {
    public static final String DOMAIN = "ZBB";           // 缓存域名前缀

    private CacheKeyGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String generate(String key) {
        return String.format("%s::%s", DOMAIN, key.toUpperCase());
    }

    public static String generate(String key, String value) {
        return String.format("%s::%s:%s", DOMAIN, key.toUpperCase(), value);
    }

    public static String generate(String key, short value) {
        return generate(key, String.valueOf(value));
    }

    public static String generate(String key, int value) {
        return generate(key, String.valueOf(value));
    }

    public static String generate(String key, long value) {
        return generate(key, String.valueOf(value));
    }

    public static String generate(String key, double value) {
        return generate(key, String.valueOf(value));
    }

    public static String generate(String key, Object... values) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringBuilder value = new StringBuilder();
            for (Object val : values) {
                value.append(objectMapper.writeValueAsString(val)).append(",");
            }
            return CacheKeyGenerator.generate(key, value.toString());
        } catch (Exception e) {
            throw new RuntimeException("生成缓存键失败", e);
        }
    }

    // 我是分割线 --------------------------------------------------------------------------------------------------------

    /*
     * 生成账号登录凭证缓存键
     *
     * @param scope 作用域（如：ADMIN、USER等）
     */
    public static String getAccountTokenKey(String scope) {
        return generate(scope.toUpperCase() + "-TOKENS");
    }

    /**
     * 账号登录凭证缓存键
     *
     * @param scope  作用域（如：ADMIN、USER等）
     * @param userId 用户ID
     */
    public static String getAuthUserKey(String scope, long userId) {
        return generate(scope.toUpperCase() + "-AUTHED", userId);
    }

    /**
     * 账号登陆错误次数缓存键
     *
     * @param scope   作用域（如：ADMIN、USER等）
     * @param account 账号
     */
    public static String getAccountFailedAttempLimitKey(String scope, String account) {
        return generate(scope.toUpperCase() + "-ACCOUNT-FAILED", account);
    }

    /**
     * 客户端IP登录错误次数缓存键
     *
     * @param scope 作用域（如：ADMIN、USER等）
     * @param ip    客户端IP地址
     */
    public static String getIpFailedAttempLimitKey(String scope, String ip) {
        return generate(scope.toUpperCase() + "-IP-FAILED", IPUtils.ipv4ToLong(ip.getBytes()));
    }

}

