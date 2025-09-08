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

import com.zhenbanban.bossapi.vo.ChineseMedicinePieceSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.ChineseMedicinePieceAmdCmdHandler;
import com.zhenbanban.core.application.dto.*;
import com.zhenbanban.core.application.query.ChineseMedicinePieceQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.annotation.InList;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制器 : 中药饮片
 *
 * @author zhangxihai 2025/08/31
 */
@RestController
@RequestMapping("/chinese-medicine-pieces")
public class ChineseMedicinePieceController {
    private final ChineseMedicinePieceAmdCmdHandler chineseMedicinePieceAmdCmdHandler;
    private final ChineseMedicinePieceQueryHandler chineseMedicinePieceQueryHandler;

    @Autowired
    public ChineseMedicinePieceController(
            @Lazy ChineseMedicinePieceAmdCmdHandler chineseMedicinePieceAmdCmdHandler,
            @Lazy ChineseMedicinePieceQueryHandler chineseMedicinePieceQueryHandler
    ) {
        this.chineseMedicinePieceAmdCmdHandler = chineseMedicinePieceAmdCmdHandler;
        this.chineseMedicinePieceQueryHandler = chineseMedicinePieceQueryHandler;
    }

    /**
     * 添加中药饮片
     *
     * @param request 中药饮片信息
     * @return 中药饮片ID
     */
    @PostMapping
    @AdminPermit(permissions = {"chinese-medicine-piece:add"}, message = "您未被授权执行此操作：添加中药饮片")
    public IdResponse addChineseMedicinePiece(@Valid @RequestBody ChineseMedicinePieceSaveRequest request) {
        ChineseMedicinePieceAmdCommand command = (new ModelMapper()).map(request, ChineseMedicinePieceAmdCommand.class);
        Long pieceId = chineseMedicinePieceAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(pieceId).build();
    }

    /**
     * 更新中药饮片
     *
     * @param request 中药饮片信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"chinese-medicine-piece:modify"}, message = "您未被授权执行此操作：修改中药饮片信息")
    public void modifyChineseMedicinePiece(@PathVariable("id") Long id, @Valid @RequestBody ChineseMedicinePieceSaveRequest request) {
        ChineseMedicinePieceAmdCommand command = (new ModelMapper()).map(request, ChineseMedicinePieceAmdCommand.class);
        command.setId(id);
        chineseMedicinePieceAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除中药饮片
     *
     * @param id 中药饮片ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"chinese-medicine-piece:delete"}, message = "您未被授权执行此操作：删除中药饮片")
    public void deleteChineseMedicinePiece(@PathVariable("id") Long id) {
        chineseMedicinePieceAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取中药饮片
     *
     * @param id 中药饮片ID
     * @return 中药饮片信息
     */
    @GetMapping("/{id:\\d+}")
    @AdminPermit(permissions = {"chinese-medicine-piece:add", "chinese-medicine-piece:modify", "chinese-medicine-piece:delete"}, message = "您未被授权执行此操作：查询中药饮片")
    public ChineseMedicinePieceDto getChineseMedicinePiece(@PathVariable("id") Long id) {
        return chineseMedicinePieceQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取中药饮片分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param keywords 关键词
     * @return 中药饮片分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"chinese-medicine-piece:add", "chinese-medicine-piece:modify", "chinese-medicine-piece:delete"}, message = "您未被授权执行此操作：查询中药饮片")
    public Pagination<ChineseMedicinePieceDto> getChineseMedicinePiecePagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "deleted", defaultValue = "false", required = false) boolean deleted
    ) {
        ChineseMedicinePieceQuery query = ChineseMedicinePieceQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .deleted(deleted)
                .build();

        return chineseMedicinePieceQueryHandler.handleQueryPage(query);
    }

    @GetMapping("/options")
    @AdminPermit
    public List<ChineseMedicinePieceOptionDto> getChineseMedicinePieceOptions(
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords
    ) {
        ChineseMedicinePieceoOptionQuery query = ChineseMedicinePieceoOptionQuery.builder()
                .keywords(keywords)
                .build();

        return chineseMedicinePieceQueryHandler.handleQueryOption(query);
    }

}
