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

import com.zhenbanban.bossapi.vo.AdminRolesBindingRequest;
import com.zhenbanban.bossapi.vo.AdminSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.AdminAmdCmdHandler;
import com.zhenbanban.core.application.command.AdminRolesBindingCmdHandler;
import com.zhenbanban.core.application.dto.AdminAmdCommand;
import com.zhenbanban.core.application.dto.AdminRolesBindingCommand;
import com.zhenbanban.core.application.query.AdminMenuQueryHandler;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.shared.contract.IAuth;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * Controller : 管理员
 *
 * @author zhangxihai 2025/8/4
 */
@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminAmdCmdHandler adminAmdCmdHandler;

    private final AdminRolesBindingCmdHandler adminRolesBindingCmdHandler;

    private final IAuth<Admin> auth;

    private final AdminMenuQueryHandler adminMenuQueryHandler;

    public AdminController(
            @Lazy AdminAmdCmdHandler adminAmdCmdHandler,
            @Lazy AdminRolesBindingCmdHandler adminRolesBindingCmdHandler,
            @Lazy IAuth<Admin> auth,
            @Lazy AdminMenuQueryHandler adminMenuQueryHandler
    ) {
        this.adminAmdCmdHandler = adminAmdCmdHandler;
        this.adminRolesBindingCmdHandler = adminRolesBindingCmdHandler;
        this.auth = auth;
        this.adminMenuQueryHandler = adminMenuQueryHandler;
    }

    /**
     * 添加管理员
     *
     * @param request 管理员信息
     * @return 管理员实体
     */
    @PostMapping
    @AdminPermit(permissions = {"admin:add"}, message = "您未被授权执行此操作：添加管理员")
    public IdResponse addAdmin(@Valid @RequestBody AdminSaveRequest request) {
        AdminAmdCommand command = AdminAmdCommand.builder()
                .id(request.getAccountId())
                .build();
        return new IdResponse(adminAmdCmdHandler.handleAdd(command));
    }

    /**
     * 更新管理员
     *
     * @param request 管理员信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"admin:add"}, message = "您未被授权执行此操作：修改管理员信息")
    public void modifyAdmin(@PathVariable("id") Long id, @Valid @RequestBody AdminSaveRequest request) {
        AdminAmdCommand command = AdminAmdCommand.builder()
                .id(id)
                .build();
        adminAmdCmdHandler.handleModify(command);
    }

    /**
     * 禁用管理员
     *
     * @param id 管理员ID
     */
    @PostMapping("/{id}/lock")
    @AdminPermit(permissions = {"admin:modify"}, message = "您未被授权执行此操作：禁用管理员")
    public void deleteAdmin(@PathVariable("id") Long id) {
        adminAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 启用管理员
     *
     * @param id 管理员ID
     */
    @DeleteMapping("/{id}/lock")
    @AdminPermit(permissions = {"admin:delete"}, message = "您未被授权执行此操作：激活管理员")
    public void activateAdmin(@PathVariable("id") Long id) {
        adminAmdCmdHandler.handleActivate(id);
    }

    /**
     * 修改管理员角色
     *
     * @param id      管理员ID
     * @param request 管理员角色绑定请求
     */
    @PutMapping("/{id}/roles")
    @AdminPermit(permissions = {"admin:activate"}, message = "您未被授权执行此操作：为管理员分配角色")
    public void modifyAdminRoles(@PathVariable("id") Long id, @RequestBody AdminRolesBindingRequest request) {
        AdminRolesBindingCommand command = AdminRolesBindingCommand.builder()
                .id(id)
                .roleIds(request.getRoleIds())
                .build();

        adminRolesBindingCmdHandler.handle(command);
    }

}
