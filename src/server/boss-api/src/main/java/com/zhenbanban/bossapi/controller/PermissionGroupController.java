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
import com.zhenbanban.bossapi.vo.PermissionGroupSaveRequest;
import com.zhenbanban.core.application.command.PermissionGroupAmdCmdHandler;
import com.zhenbanban.core.application.dto.PermissionGroupAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 权限组
 *
 * @author zhangxihai 2025/7/11
 */
@RestController
@RequestMapping("/permission-groups")
public class PermissionGroupController {
    private final PermissionGroupAmdCmdHandler permissionGroupAmdCmdHandler;

    @Autowired
    public PermissionGroupController(@Lazy PermissionGroupAmdCmdHandler permissionGroupAmdCmdHandler) {
        this.permissionGroupAmdCmdHandler = permissionGroupAmdCmdHandler;
    }

    /**
     * 创建权限组
     *
     * @param request 请求
     * @return ID响应
     */
    @PostMapping
    @AdminPermit(permissions = {"permission-group:add"}, message = "您未被授权执行此操作：添加权限组")
    public IdResponse createPermissionGroup(@Valid @RequestBody PermissionGroupSaveRequest request) {
        PermissionGroupAmdCommand command = (new ModelMapper()).map(request, PermissionGroupAmdCommand.class);
        Long id = permissionGroupAmdCmdHandler.handleAdd(command);
        return IdResponse.builder()
                .id(id)
                .build();
    }

    /**
     * 修改权限组
     *
     * @param request 请求
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"permission-group:modify"}, message = "您未被授权执行此操作：修改权限组信息")
    public void modifyPermissionGroup(@PathVariable("id") Long id, @Valid @RequestBody PermissionGroupSaveRequest request) {
        PermissionGroupAmdCommand command = (new ModelMapper()).map(request, PermissionGroupAmdCommand.class);
        command.setId(id);
        permissionGroupAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除权限组
     *
     * @param id 权限组ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"permission-group:delete"}, message = "您未被授权执行此操作：删除权限组")
    public void destroyPermissionGroup(@PathVariable("id") Long id) {
        permissionGroupAmdCmdHandler.handleDestroy(id);
    }

}
