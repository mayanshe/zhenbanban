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
package com.zhenbanban.core.domain.accountcontext.listener;

import com.zhenbanban.core.domain.accountcontext.event.AccountLoggedInEvent;
import com.zhenbanban.core.domain.accountcontext.event.AccountLoggedOutEvent;
import com.zhenbanban.core.infrastructure.persistence.mapper.AccountLoginLogPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.AccountLoginLogPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Listener : 账号尝试事件监听器
 *
 * @author zhangxihai 2025/8/4
 */
@Component
public class AccountAttemptEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AdminEventListener.class);

    private final AccountLoginLogPoMapper accountLoginLogPoMapper;

    public AccountAttemptEventListener(
            @Lazy AccountLoginLogPoMapper accountLoginLogPoMapper
    ) {
        this.accountLoginLogPoMapper = accountLoginLogPoMapper;
    }

    @EventListener
    @Async
    public void handleAccountLoggedInEvent(AccountLoggedInEvent event) {
        logger.info("响应事件 AccountLoggedInEvent: ID={}", event.getEventId());

        try {
            AccountLoginLogPo accountLoginLogPo = AccountLoginLogPo.builder()
                    .accountId(event.getAccountId())
                    .username(event.getUsername())
                    .scope(event.getScope())
                    .kind(event.getKind())
                    .platform(event.getPlatform())
                    .clientIp(event.getClientIp())
                    .userAgent(event.getUserAgent())
                    .browserFingerprint(event.getBrowserFingerprint())
                    .tokenMd5(event.getTokenMd5())
                    .loginAt(System.currentTimeMillis())
                    .build();
            accountLoginLogPoMapper.insert(accountLoginLogPo);
        } catch (Exception e) {
            logger.error("响应事件错误: {} ", e.getMessage() + ":" + event.getEventId(), e);
        }
    }

    @EventListener
    @Async
    public void handleAccountLoggedOutEvent(AccountLoggedOutEvent event) {
        logger.info("响应事件 AccountLoggedOutEvent: ID={}", event.getEventId());

        try {
            Map<String, Object> params = Map.of(
                    "accountId", event.getAccountId(),
                    "tokenMd5", event.getTokenMd5(),
                    "logoutAt", System.currentTimeMillis()
            );
            accountLoginLogPoMapper.update(params);
        } catch (Exception e) {
            logger.error("响应事件错误: {} ", e.getMessage() + ":" + event.getEventId(), e);
        }
    }

}
