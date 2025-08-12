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
package com.zhenbanban.core.domain.accountcontext.service.impl;

import com.zhenbanban.core.domain.accountcontext.service.AccountLoginAttemptServ;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.DateAndTimeUtils;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Domain Service Achieve : 账号登陆尝试服务
 *
 * @author zhangxihai 2025/8/4
 */
@Service
public class AccountLoginAttemptServImpl implements AccountLoginAttemptServ {
    private final RedisUtils redisUtils;

    public AccountLoginAttemptServImpl(@Lazy RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void checkLoginAttempts(String scope, String account, int accountLimit, String clientIp, int clientIpLimit) {
        String accountKey = CacheKeyGenerator.getAccountFailedAttemptLimitKey(scope, account);
        String clientIpKey = CacheKeyGenerator.getIpFailedAttempLimitKey(scope, clientIp);

        int accountAttempts = redisUtils.get(accountKey, Integer.class, 0);
        int clientIpAttempts = redisUtils.get(clientIpKey, Integer.class, 0);

        if (accountAttempts >= accountLimit || clientIpAttempts >= clientIpLimit) {
            throw new BadRequestException("登陆错误次数已达本日上限，请明天再试或联系管理员处理。");
        }
    }

    @Override
    public void recordFailedAttempt(String scope, String account, String clienIp) {
        String accountKey = CacheKeyGenerator.getAccountFailedAttemptLimitKey(scope, account);
        if (redisUtils.hasKey(accountKey)) {
            redisUtils.incr(accountKey, 1);
        } else {
            redisUtils.set(accountKey, 1, DateAndTimeUtils.getSecondsToEndOfDay());  // 到今天23:59结束
        }

        String clientIpKey = CacheKeyGenerator.getIpFailedAttempLimitKey(scope, clienIp);
        if (redisUtils.hasKey(clientIpKey)) {
            redisUtils.incr(clientIpKey, 1);
        } else {
            redisUtils.set(clientIpKey, 1, DateAndTimeUtils.getSecondsToEndOfDay());  // 到今天23:59结束
        }
    }

    @Override
    public void clearFailedAttempts(String scope, String account, String clientIp) {
        String accountKey = CacheKeyGenerator.getAccountFailedAttemptLimitKey(scope, account);
        if (redisUtils.hasKey(accountKey)) {
            redisUtils.del(accountKey);
        }

        String clientIpKey = CacheKeyGenerator.getIpFailedAttempLimitKey(scope, clientIp);
        if (redisUtils.hasKey(clientIpKey)) {
            redisUtils.del(clientIpKey);
        }
    }

}
