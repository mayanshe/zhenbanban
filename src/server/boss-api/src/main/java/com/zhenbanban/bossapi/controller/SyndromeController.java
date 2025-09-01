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

import com.zhenbanban.bossapi.vo.SyndromeSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.SyndromeAmdCmdHandler;
import com.zhenbanban.core.application.dto.SyndromeAmdCommand;
import com.zhenbanban.core.application.dto.SyndromeDto;
import com.zhenbanban.core.application.dto.SyndromeQuery;
import com.zhenbanban.core.application.query.SyndromeQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 中医证候
 *
 * @author zhangxihai 2025/09/01
 */
@RestController
@RequestMapping("/syndromes")
public class SyndromeController {
    private final SyndromeAmdCmdHandler syndromeAmdCmdHandler;
    private final SyndromeQueryHandler syndromeQueryHandler;

    @Autowired
    public SyndromeController(
            @Lazy SyndromeAmdCmdHandler syndromeAmdCmdHandler,
            @Lazy SyndromeQueryHandler syndromeQueryHandler
    ) {
        this.syndromeAmdCmdHandler = syndromeAmdCmdHandler;
        this.syndromeQueryHandler = syndromeQueryHandler;
    }

    /**
     * 添加中医证候
     *
     * @param request 中医证候信息
     * @return 中医证候ID
     */
    @PostMapping
    @AdminPermit(permissions = {"syndrome:add"}, message = "您未被授权执行此操作：添加中医证候")
    public IdResponse addSyndrome(@Valid @RequestBody SyndromeSaveRequest request) {
        SyndromeAmdCommand command = (new ModelMapper()).map(request, SyndromeAmdCommand.class);
        Long syndromeId = syndromeAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(syndromeId).build();
    }

    /**
     * 更新中医证候
     *
     * @param request 中医证候信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"syndrome:modify"}, message = "您未被授权执行此操作：修改中医证候信息")
    public void modifySyndrome(@PathVariable("id") Long id, @Valid @RequestBody SyndromeSaveRequest request) {
        SyndromeAmdCommand command = (new ModelMapper()).map(request, SyndromeAmdCommand.class);
        command.setId(id);
        syndromeAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除中医证候
     *
     * @param id 中医证候ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"syndrome:delete"}, message = "您未被授权执行此操作：删除中医证候")
    public void deleteSyndrome(@PathVariable("id") Long id) {
        syndromeAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取中医证候
     *
     * @param id 中医证候ID
     * @return 中医证候信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"syndrome:add", "syndrome:modify", "syndrome:delete"}, message = "您未被授权执行此操作：查询中医证候")
    public SyndromeDto getSyndrome(@PathVariable("id") Long id) {
        return syndromeQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取中医证候分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param keywords 关键词
     * @return 中医证候分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"syndrome:add", "syndrome:modify", "syndrome:delete"}, message = "您未被授权执行此操作：查询中医证候列表")
    public Pagination<SyndromeDto> getSyndromePagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "syndromeCode", defaultValue = "", required = false) String syndromeCode,
            @RequestParam(value = "deleted", defaultValue = "false", required = false) boolean deleted
    ) {
        SyndromeQuery query = SyndromeQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .syndromeCode(syndromeCode)
                .deleted(deleted)
                .build();

        return syndromeQueryHandler.handleQueryPage(query);
    }

}
