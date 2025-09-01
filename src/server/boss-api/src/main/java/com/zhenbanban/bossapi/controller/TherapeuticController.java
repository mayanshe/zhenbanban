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
import com.zhenbanban.bossapi.vo.TherapeuticSaveRequest;
import com.zhenbanban.core.application.command.TherapeuticAmdCmdHandler;
import com.zhenbanban.core.application.dto.TherapeuticAmdCommand;
import com.zhenbanban.core.application.dto.TherapeuticDto;
import com.zhenbanban.core.application.dto.TherapeuticQuery;
import com.zhenbanban.core.application.query.TherapeuticQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器: 中医治法
 *
 * @author zhangxihai 2025/09/01
 */
@RestController
@RequestMapping("/therapeutics")
public class TherapeuticController {
    private final TherapeuticAmdCmdHandler therapeuticAmdCmdHandler;
    private final TherapeuticQueryHandler therapeuticQueryHandler;

    @Autowired
    public TherapeuticController(
            @Lazy TherapeuticAmdCmdHandler therapeuticAmdCmdHandler,
            @Lazy TherapeuticQueryHandler therapeuticQueryHandler
    ) {
        this.therapeuticAmdCmdHandler = therapeuticAmdCmdHandler;
        this.therapeuticQueryHandler = therapeuticQueryHandler;
    }

    /**
     * 添加中医治法
     *
     * @param request 中医治法信息
     * @return 中医治法ID
     */
    @PostMapping
    @AdminPermit(permissions = {"therapeutic:add"}, message = "您未被授权执行此操作：添加中医治法")
    public IdResponse addTherapeutic(@Valid @RequestBody TherapeuticSaveRequest request) {
        TherapeuticAmdCommand command = (new ModelMapper()).map(request, TherapeuticAmdCommand.class);
        Long therapeuticId = therapeuticAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(therapeuticId).build();
    }

    /**
     * 更新中医治法
     *
     * @param request 中医治法信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"therapeutic:modify"}, message = "您未被授权执行此操作：修改中医治法信息")
    public void modifyTherapeutic(@PathVariable("id") Long id, @Valid @RequestBody TherapeuticSaveRequest request) {
        TherapeuticAmdCommand command = (new ModelMapper()).map(request, TherapeuticAmdCommand.class);
        command.setId(id);
        therapeuticAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除中医治法
     *
     * @param id 中医治法ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"therapeutic:delete"}, message = "您未被授权执行此操作：删除中医治法")
    public void deleteTherapeutic(@PathVariable("id") Long id) {
        therapeuticAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取中医治法
     *
     * @param id 中医治法ID
     * @return 中医治法信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"therapeutic:add", "therapeutic:modify", "therapeutic:delete"}, message = "您未被授权执行此操作：查询中医治法")
    public TherapeuticDto getTherapeutic(@PathVariable("id") Long id) {
        return therapeuticQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取中医治法分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param keywords 关键词
     * @return 中医治法分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"therapeutic:add", "therapeutic:modify", "therapeutic:delete"}, message = "您未被授权执行此操作：查询中医治法")
    public Pagination<TherapeuticDto> getTherapeuticPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "therapeuticsCode", defaultValue = "", required = false) String therapeuticsCode,
            @RequestParam(value = "deleted", defaultValue = "false", required = false) boolean deleted
    ) {
        TherapeuticQuery query = TherapeuticQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .therapeuticsCode(therapeuticsCode)
                .deleted(deleted)
                .build();

        return therapeuticQueryHandler.handleQueryPage(query);
    }
}
