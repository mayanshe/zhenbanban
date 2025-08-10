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
import com.zhenbanban.bossapi.vo.RoleSaveRequest;
import com.zhenbanban.core.application.command.RoleAmdCmdHandler;
import com.zhenbanban.core.application.dto.RoleAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器: 系统角色管理
 *
 * @author zhangxihai 2025/08/02
 */
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleAmdCmdHandler roleAmdCmdHandler;

    @Autowired
    public RoleController(@Lazy RoleAmdCmdHandler roleAmdCmdHandler) {
        this.roleAmdCmdHandler = roleAmdCmdHandler;
    }

    /**
     * 创建角色
     *
     * @param request 角色信息
     * @return 角色ID
     */
    @PostMapping
    @AdminPermit(permissions = {"role:add"}, message = "您未被授权执行此操作：添加角色")
    public IdResponse addRole(@Valid @RequestBody RoleSaveRequest request) {
        RoleAmdCommand command = (new ModelMapper()).map(request, RoleAmdCommand.class);
        Long roleId = roleAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(roleId).build();
    }

    /**
     * 修改角色
     *
     * @param id 角色ID
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"role:add"}, message = "您未被授权执行此操作：修改角色信息")
    public void modifyRole(@PathVariable("id") Long id, @Valid @RequestBody RoleSaveRequest request) {
        RoleAmdCommand command = (new ModelMapper()).map(request, RoleAmdCommand.class);
        command.setId(id);
        roleAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"role:add"}, message = "您未被授权执行此操作：删除角色")
    public void destroyRole(@PathVariable("id") Long id) {
        roleAmdCmdHandler.handleDestroy(id);
    }

}
