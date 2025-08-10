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
import com.zhenbanban.bossapi.vo.PermissionSaveRequest;
import com.zhenbanban.core.application.command.PermissionAmdCmdHandler;
import com.zhenbanban.core.application.dto.PermissionAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 权限
 *
 * @author zhangxihai 2025/7/11
 */
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionAmdCmdHandler permissionAmdCmdHandler;

    @Autowired
    public PermissionController(@Lazy PermissionAmdCmdHandler permissionAmdCmdHandler) {
        this.permissionAmdCmdHandler = permissionAmdCmdHandler;
    }

    /**
     * 添加权限
     *
     * @param request 权限信息
     * @return 权限ID
     */
    @PostMapping
    @AdminPermit(permissions = {"permission:add"}, message = "您未被授权执行此操作：添加权限")
    public IdResponse addPermission(@Valid @RequestBody PermissionSaveRequest request) {
        PermissionAmdCommand command = (new ModelMapper()).map(request, PermissionAmdCommand.class);
        Long permissionId = permissionAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(permissionId).build();
    }

    /**
     * 更新权限
     *
     * @param request 权限信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"permission:modify"}, message = "您未被授权执行此操作：修改权限信息")
    public void modifyPermission(@PathVariable("id") Long id, @Valid @RequestBody PermissionSaveRequest request) {
        PermissionAmdCommand command = (new ModelMapper()).map(request, PermissionAmdCommand.class);
        command.setId(id);
        permissionAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"permission:delete"}, message = "您未被授权执行此操作：删除权限")
    public void deletePermission(@PathVariable("id") Long id) {
        permissionAmdCmdHandler.handleDestroy(id);
    }

}
