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

import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.bossapi.vo.AccountAddRequest;
import com.zhenbanban.bossapi.vo.ModifyAccountRequest;
import com.zhenbanban.core.application.command.AccountAmdCmdHandler;
import com.zhenbanban.core.application.dto.AccountAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * Controller : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountAmdCmdHandler accountAmdCmdHandler;

    public AccountController(
            @Lazy AccountAmdCmdHandler accountAmdCmdHandler
    ) {
        this.accountAmdCmdHandler = accountAmdCmdHandler;
    }

    /**
     * 添加账号
     *
     * @param request 账号信息
     * @return 账号ID
     */
    @PostMapping
    @AdminPermit(permissions = {"account:add"}, message = "您未被授权执行此操作：添加用户账号")
    public IdResponse addAccount(@Valid @RequestBody AccountAddRequest request) {
        AccountAmdCommand command = (new ModelMapper()).map(request, AccountAmdCommand.class);
        Long accountId = accountAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(accountId).build();
    }

    /**
     * 更新账号
     *
     * @param id      账号ID
     * @param request 账号信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"account:modify"}, message = "您未被授权执行此操作：修改用户账号信息")
    public void updateAccount(@PathVariable("id") Long id, @Valid @RequestBody ModifyAccountRequest request) {
        AccountAmdCommand command = (new ModelMapper()).map(request, AccountAmdCommand.class);
        command.setId(id);
        accountAmdCmdHandler.handleModify(command);
    }

    /**
     * 禁用账号
     *
     * @param id 账号ID
     */
    @PostMapping("/{id}/lock")
    @AdminPermit(permissions = {"account:delete"}, message = "您未被授权执行此操作：禁用用户账号")
    public void deleteAccount(@PathVariable("id") Long id) {
        accountAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 激活账号
     *
     * @param id 账号ID
     */
    @DeleteMapping("/{id}/lock")
    @AdminPermit(permissions = {"account:activate"}, message = "您未被授权执行此操作：激活用户账号")
    public void deleteAccountForbidden(@PathVariable("id") Long id) {
        accountAmdCmdHandler.handleActivate(id);
    }

}
