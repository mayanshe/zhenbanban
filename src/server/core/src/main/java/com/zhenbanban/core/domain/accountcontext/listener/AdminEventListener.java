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

import com.zhenbanban.core.domain.accountcontext.entity.Account;
import com.zhenbanban.core.domain.accountcontext.event.AdminActivatedEvent;
import com.zhenbanban.core.domain.accountcontext.event.AdminAddedEvent;
import com.zhenbanban.core.domain.accountcontext.repository.AccountRepository;
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 类型 : AdminEventListener
 *
 * @author zhangxihai 2025/8/4
 */
@Component
public class AdminEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AdminEventListener.class);

    private final AccountRepository accountRepository;

    public AdminEventListener(
            @Lazy AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    /**
     * 响应管理员欻功能键成功事件
     *
     * @param event 管理员激活事件
     */
    @EventListener
    public void handleAdminAddedEvent(AdminAddedEvent event) {
        logger.info("响应事件 AdminCreatedEvent: ID={}", event.getEventId());
        Long adminId = event.getAdminId();

        // 处理管理员创建事件
        try {
            addAccountScope(adminId);
        } catch (Exception e) {
            logger.error("响应事件错误: {} ", e.getMessage() + ":" + adminId, e);
        }
    }

    /**
     * 添加管理员账号的admin权限范围
     *
     * @param adminId 管理员ID
     */
    private void addAccountScope(Long adminId) {
        Account account = accountRepository.load(adminId);
        account.addScope("admin");
        accountRepository.save(account, false);
    }

    /**
     * 移除管理员账号的admin权限范围
     *
     * @param adminId 管理员ID
     */
    private void removeAccountScope(Long adminId) {
        Account account = accountRepository.load(adminId);
        account.removeScope("admin");
        accountRepository.save(account, false);
    }

}
