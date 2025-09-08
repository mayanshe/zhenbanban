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

import com.zhenbanban.bossapi.vo.AntagonismSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.AntagonismAmdCmdHandler;
import com.zhenbanban.core.application.dto.AntagonismAmdCommand;
import com.zhenbanban.core.application.dto.AntagonismDto;
import com.zhenbanban.core.application.dto.AntagonismQuery;
import com.zhenbanban.core.application.query.AntagonismQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@RestController
@RequestMapping("/antagonisms")
public class AntagonismController {

    private final AntagonismAmdCmdHandler antagonismAmdCmdHandler;

    private final AntagonismQueryHandler antagonismQueryHandler;

    @Autowired
    public AntagonismController(
            @Lazy AntagonismAmdCmdHandler antagonismAmdCmdHandler,
            @Lazy AntagonismQueryHandler antagonismQueryHandler
    ) {
        this.antagonismAmdCmdHandler = antagonismAmdCmdHandler;
        this.antagonismQueryHandler = antagonismQueryHandler;
    }

    /**
     * 添加十八反十九畏
     *
     * @param request 信息
     * @return ID
     */
    @PostMapping
    @AdminPermit(permissions = {"antagonism:add"}, message = "您未被授权执行此操作：添加十八反十九畏")
    public IdResponse addAntagonism(@Valid @RequestBody AntagonismSaveRequest request) {
        AntagonismAmdCommand command = (new ModelMapper()).map(request, AntagonismAmdCommand.class);
        Long antagonismId = antagonismAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(antagonismId).build();
    }

    /**
     * 更新十八反十九畏
     *
     * @param request 信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"antagonism:modify"}, message = "您未被授权执行此操作：修改十八反十九畏信息")
    public void modifyAntagonism(@PathVariable("id") Long id, @Valid @RequestBody AntagonismSaveRequest request) {
        AntagonismAmdCommand command = (new ModelMapper()).map(request, AntagonismAmdCommand.class);
        command.setId(id);
        antagonismAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除十八反十九畏
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"antagonism:delete"}, message = "您未被授权执行此操作：删除十八反十九畏")
    public void deleteAntagonism(@PathVariable("id") Long id) {
        antagonismAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取十八反十九畏
     *
     * @param id ID
     * @return 信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"antagonism:view"}, message = "您未被授权执行此操作：查询十八反十九畏")
    public AntagonismDto getAntagonism(@PathVariable("id") Long id) {
        return antagonismQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取十八反十九畏分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param type     类型
     * @param keywords 关键词
     * @return 分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"antagonism:view"}, message = "您未被授权执行此操作：查询十八反十九畏")
    public Pagination<AntagonismDto> getAntagonismPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "kind", required = false) Short kind,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords
    ) {
        AntagonismQuery query = AntagonismQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .kind(kind)
                .build();
        return antagonismQueryHandler.handleQueryPage(query);
    }

}
