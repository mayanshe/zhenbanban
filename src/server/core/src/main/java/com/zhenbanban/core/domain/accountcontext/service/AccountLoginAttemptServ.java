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
package com.zhenbanban.core.domain.accountcontext.service;

import org.springframework.stereotype.Service;

/**
 * Domain Service Interface : 账号登陆尝试服务
 *
 * @author zhangxihai 2025/8/4
 */
@Service
public interface AccountLoginAttemptServ {
    /**
     * 检查登录尝试次数
     *
     * @param scope         作用域（如：管理员、用户等）
     * @param account       账号
     * @param accountLimit  账号登录尝试限制次数
     * @param clientIp      客户端IP地址
     * @param clientIpLimit 客户端IP地址登录尝试限制次数
     */
    void checkLoginAttempts(String scope, String account, int accountLimit, String clientIp, int clientIpLimit);

    /**
     * 记录账号登录失败尝试
     *
     * @param scope    作用域（如：管理员、用户等）
     * @param account  账号
     * @param clientIp 客户端IP地址
     */
    void recordFailedAttempt(String scope, String account, String clientIp);

    /**
     * 清除账号登录失败尝试次数
     *
     * @param scope    作用域（如：管理员、用户等）
     * @param account  账号
     * @param clientIp 客户端IP地址
     */
    void clearFailedAttempts(String scope, String account, String clientIp);


}
