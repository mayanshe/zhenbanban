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
package com.zhenbanban.core.shared.valueobj;

/**
 * Shared Value Object : JwtSetting
 *
 * @author zhangxihai 2025/8/4
 */
public class JwtSetting {
    private final String type;

    private final String secret;

    private Long expire = 360L;

    private Long rememberExpire = 10080L;

    private Long refreshExpire = 20160L;

    public JwtSetting(String Type, String secret, Long expire, Long rememberExpire, Long refreshExpire) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("请设置有效的 JWT 密钥");
        }

        if (expire == null || expire <= 0) {
            throw new IllegalArgumentException("请设置有效的 JWT TOKEN 过期时间");
        }

        if (rememberExpire == null || rememberExpire < 0) {
            throw new IllegalArgumentException("请设置有效的 JWT TOKEN 记住我过期时间");
        }

        if (refreshExpire == null || refreshExpire <= 0) {
            throw new IllegalArgumentException("请设置有效的 JWT TOKEN 刷新过期时间");
        }

        if (rememberExpire <= expire) {
            throw new IllegalArgumentException("JWT TOKEN 过期时间必须小于等于记住我过期时间");
        }

        if (refreshExpire <= expire) {
            throw new IllegalArgumentException("JWT TOKEN 刷新过期时间必须大于过期时间");
        }

        this.type = Type;
        this.secret = secret;
        this.expire = expire;
        this.rememberExpire = rememberExpire;
        this.refreshExpire = refreshExpire;
    }

    public String getType() {
        return type;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpire() {
        return expire;
    }

    public Long getRememberExpire() {
        return rememberExpire;
    }

    public Long getRefreshExpire() {
        return refreshExpire;
    }

}
