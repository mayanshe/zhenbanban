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
package com.zhenbanban.core.application.command.impl;

import com.zhenbanban.core.application.command.QuickMenuAddHandler;
import com.zhenbanban.core.application.dto.QuickMenuAddCommand;
import com.zhenbanban.core.domain.systemcontext.valueobj.QuickMenu;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Command Handler Achieve : QuickMenuAddHandlerImpl
 *
 * @author zhangxihai 2025/9/8
 */
@Service
@RequiredArgsConstructor
public class QuickMenuAddHandlerImpl implements QuickMenuAddHandler {
    private final RedisUtils redisUtils;

    public void handle(QuickMenuAddCommand command) {
        String cacheKey = CacheKeyGenerator.getAdminQuickMenuKey(command.getUserId());

        List<Object> menus = redisUtils.lGet(cacheKey, 0, -1);

        // 移除已存在的快捷键
        for (Object menuObj : menus) {
            if (menuObj instanceof QuickMenu menu) {
                if (menu.getRouteName().equals(command.getRouteName())) {
                    redisUtils.lRemove(cacheKey, 8, menuObj);
                    break;
                }
            }
        }

        // 在头部插入新的快捷键
        QuickMenu newMenu = new QuickMenu(command.getPageName(), command.getRouteName());
        redisUtils.lLPush(cacheKey, newMenu, 0);

        // 保持快捷键数量不超过8个
        long menuCount = redisUtils.lGetListSize(cacheKey);
        if (menuCount > 8) {
            redisUtils.lRemoveRange(cacheKey, 0, 7);
        }
    }

}
