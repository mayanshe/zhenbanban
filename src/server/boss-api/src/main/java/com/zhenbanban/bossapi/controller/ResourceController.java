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
import com.zhenbanban.bossapi.vo.ResourceSaveRequest;
import com.zhenbanban.core.application.command.ResourceAmdCmdHandler;
import com.zhenbanban.core.application.dto.ResourceAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * Controller : 资源
 *
 * @author zhangxihai 2025/7/11
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceAmdCmdHandler resourceAmdCmdHandler;

    public ResourceController(
            @Lazy ResourceAmdCmdHandler resourceAmdCmdHandler
    ) {
        this.resourceAmdCmdHandler = resourceAmdCmdHandler;
    }

    /**
     * 添加资源
     *
     * @param request 资源信息
     * @return 资源ID
     */
    @PostMapping
    @AdminPermit(permissions = {"resource:add"}, message = "您未被授权执行此操作：添加资源")
    public IdResponse addResource(@Valid @RequestBody ResourceSaveRequest request) {
        ResourceAmdCommand command = (new ModelMapper()).map(request, ResourceAmdCommand.class);

        Long resourceId = resourceAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(resourceId).build();
    }

    /**
     * 修改资源
     *
     * @param request 资源信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"resource:modify"}, message = "您未被授权执行此操作：修改资源信息")
    public void modifyResource(@PathVariable("id") Long id, @Valid @RequestBody ResourceSaveRequest request) {
        ResourceAmdCommand command = (new ModelMapper()).map(request, ResourceAmdCommand.class);
        command.setId(id);
        resourceAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除资源
     *
     * @param id 资源ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"resource:add"}, message = "您未被授权执行此操作：删除资源")
    public void destroyResource(@PathVariable("id") Long id) {
        resourceAmdCmdHandler.handleDestroy(id);
    }

}
