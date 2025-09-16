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
package com.zhenbanban.bossapi.controller;

import com.zhenbanban.bossapi.vo.QuickMenuRequest;
import com.zhenbanban.core.application.command.QuickMenuAddHandler;
import com.zhenbanban.core.application.dto.QuickMenuAddCommand;
import com.zhenbanban.core.application.query.QuickMenuQueryHandler;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.shared.contract.IAuth;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 类型 : QuickMenuController
 *
 * @author zhangxihai 2025/9/8
 */
@RestController
@RequestMapping("/quick-menus")
public class QuickMenuController {
    private final QuickMenuAddHandler quickMenuAddHandler;

    private final QuickMenuQueryHandler quickMenuQueryHandler;

    private final IAuth<Admin> auth;

    @Autowired
    public QuickMenuController(
            IAuth<Admin> auth,
            @Lazy QuickMenuAddHandler quickMenuAddHandler,
            @Lazy QuickMenuQueryHandler quickMenuQueryHandler
    ) {
        this.auth = auth;
        this.quickMenuAddHandler = quickMenuAddHandler;
        this.quickMenuQueryHandler = quickMenuQueryHandler;
    }

    /**
     * 添加快捷菜单
     *
     * @param request 请求参数
     */
    @PutMapping
    @AdminPermit
    public void addQuickMenu(@Valid @RequestBody QuickMenuRequest request) {
        Long adminId = auth.user().getId();
        QuickMenuAddCommand command = QuickMenuAddCommand.builder()
                .userId(adminId)
                .pageName(request.getPageName())
                .routeName(request.getRouteName())
                .build();
        quickMenuAddHandler.handle(command);
    }

    @GetMapping
    @AdminPermit
    public Object getQuickMenus() {
        Long adminId = auth.user().getId();
        return quickMenuQueryHandler.handle(adminId);
    }

}

