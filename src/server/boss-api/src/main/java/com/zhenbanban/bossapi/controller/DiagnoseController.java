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

import com.zhenbanban.bossapi.vo.DiagnoseSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.DiagnoseAmdCmdHandler;
import com.zhenbanban.core.application.dto.DiagnoseAmdCommand;
import com.zhenbanban.core.application.dto.DiagnoseDto;
import com.zhenbanban.core.application.dto.DiagnoseQuery;
import com.zhenbanban.core.application.query.DiagnoseQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 疾病诊断
 *
 * @author zhangxihai 2025/8/03
 */
@RestController
@RequestMapping("/diagnoses")
public class DiagnoseController {
    private final DiagnoseAmdCmdHandler diagnoseAmdCmdHandler;
    private final DiagnoseQueryHandler diagnoseQueryHandler;

    @Autowired
    public DiagnoseController(
            @Lazy DiagnoseAmdCmdHandler diagnoseAmdCmdHandler,
            @Lazy DiagnoseQueryHandler diagnoseQueryHandler
    ) {
        this.diagnoseAmdCmdHandler = diagnoseAmdCmdHandler;
        this.diagnoseQueryHandler = diagnoseQueryHandler;
    }

    /**
     * 添加疾病诊断
     *
     * @param request 疾病诊断信息
     * @return 疾病诊断ID
     */
    @PostMapping
    @AdminPermit(permissions = {"diagnose:add"}, message = "您未被授权执行此操作：添加疾病诊断")
    public IdResponse addDiagnose(@Valid @RequestBody DiagnoseSaveRequest request) {
        DiagnoseAmdCommand command = (new ModelMapper()).map(request, DiagnoseAmdCommand.class);
        Long diagnoseId = diagnoseAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(diagnoseId).build();
    }

    /**
     * 更新疾病诊断
     *
     * @param request 疾病诊断信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"diagnose:modify"}, message = "您未被授权执行此操作：修改疾病诊断信息")
    public void modifyDiagnose(@PathVariable("id") Long id, @Valid @RequestBody DiagnoseSaveRequest request) {
        DiagnoseAmdCommand command = (new ModelMapper()).map(request, DiagnoseAmdCommand.class);
        command.setId(id);
        diagnoseAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除疾病诊断
     *
     * @param id 疾病诊断ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"diagnose:delete"}, message = "您未被授权执行此操作：删除疾病诊断")
    public void deleteDiagnose(@PathVariable("id") Long id) {
        diagnoseAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取疾病诊断
     *
     * @param id 疾病诊断ID
     * @return 疾病诊断信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"diagnose:add", "diagnose:modify", "diagnose:delete"}, message = "您未被授权执行此操作：查询疾病诊断")
    public DiagnoseDto getDiagnose(@PathVariable("id") Long id) {
        return diagnoseQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取疾病诊断分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param icdType  ICD类型
     * @param keywords 关键词
     * @return 疾病诊断分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"diagnose:add", "diagnose:modify", "diagnose:delete"}, message = "您未被授权执行此操作：查询疾病诊断")
    public Pagination<DiagnoseDto> getDiagnosePagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "icdType", required = false) Integer icdType,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords
    ) {
        DiagnoseQuery query = DiagnoseQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .icdType(icdType)
                .keywords(keywords)
                .build();

        return diagnoseQueryHandler.handleQueryPage(query);
    }
}
