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
package com.zhenbanban.core.domain.accountcontext.valueobj;

import java.util.List;

/**
 * Value : 账号作用域
 *
 * @author zhangxihai 2025/8/3
 */
public class AccountScope {
    public static final AccountScope USER = new AccountScope("user", "用户");
    public static final AccountScope ADMIN = new AccountScope("admin", "管理员");
    public static final AccountScope DOCTOR = new AccountScope("doctor", "医生");
    public static final AccountScope ASSISTANT = new AccountScope("assistant", "医助");

    public static final List<AccountScope> ALL_SCOPES = List.of(
            USER,
            ADMIN,
            DOCTOR,
            ASSISTANT
    );

    private final String code;

    private final String name;

    public AccountScope(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 获取账号作用域名称
     *
     * @return 账号作用域名称
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取账号作用域名称
     *
     * @return 账号作用域名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取所有账号作用域
     *
     * @return 所有账号作用域
     */
    public List<AccountScope> all() {
        return ALL_SCOPES;
    }

    /**
     * 根据代码获取账号作用域
     *
     * @param code 账号作用域代码
     * @return 账号作用域
     */
    public static AccountScope of(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Account scope code cannot be null or empty");
        }

        code = code.toLowerCase();

        return switch (code) {
            case "user" -> USER;
            case "admin" -> ADMIN;
            case "doctor" -> DOCTOR;
            case "assistant" -> ASSISTANT;
            default -> throw new IllegalArgumentException("Unknown account scope code: " + code);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountScope that)) return false;
        return code.equals(that.code) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "AccountScope{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
