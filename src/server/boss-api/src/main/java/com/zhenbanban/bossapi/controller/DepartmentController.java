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

import com.zhenbanban.bossapi.vo.DepartmentSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.DepartmentAmdCmdHandler;
import com.zhenbanban.core.application.dto.DepartmentAmdCommand;
import com.zhenbanban.core.application.dto.DepartmentDto;
import com.zhenbanban.core.application.dto.DepartmentQuery;
import com.zhenbanban.core.application.query.DepartmentQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制器: 互联网医院科室
 *
 * @author zhangxihai 2025/09/16
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentAmdCmdHandler departmentAmdCmdHandler;
    private final DepartmentQueryHandler departmentQueryHandler;

    @Autowired
    public DepartmentController(
            @Lazy DepartmentAmdCmdHandler departmentAmdCmdHandler,
            @Lazy DepartmentQueryHandler departmentQueryHandler
    ) {
        this.departmentAmdCmdHandler = departmentAmdCmdHandler;
        this.departmentQueryHandler = departmentQueryHandler;
    }

    /**
     * 添加科室
     */
    @PostMapping
    @AdminPermit(permissions = {"department:add"}, message = "您未被授权执行此操作：添加科室")
    public IdResponse addDepartment(@Valid @RequestBody DepartmentSaveRequest request) {
        DepartmentAmdCommand command = (new ModelMapper()).map(request, DepartmentAmdCommand.class);
        Long departmentId = departmentAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(departmentId).build();
    }

    /**
     * 更新科室
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"department:modify"}, message = "您未被授权执行此操作：修改科室信息")
    public void modifyDepartment(@PathVariable("id") Long id, @Valid @RequestBody DepartmentSaveRequest request) {
        DepartmentAmdCommand command = (new ModelMapper()).map(request, DepartmentAmdCommand.class);
        command.setId(id);
        departmentAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除科室
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"department:delete"}, message = "您未被授权执行此操作：删除科室")
    public void deleteDepartment(@PathVariable("id") Long id) {
        departmentAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取科室
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"department:view"}, message = "您未被授权执行此操作：查询科室")
    public DepartmentDto getDepartment(@PathVariable("id") Long id) {
        return departmentQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取所有科室列表
     */
    @GetMapping("/all")
    @AdminPermit(permissions = {"department:view"}, message = "您未被授权执行此操作：查询科室")
    public List<DepartmentDto> getAllDepartments() {
        return departmentQueryHandler.handleQueryList(DepartmentQuery.builder().build());
    }

    /**
     * 获取科室分页列表
     */
    @GetMapping
    @AdminPermit(permissions = {"department:view"}, message = "您未被授权执行此操作：查询科室")
    public Pagination<DepartmentDto> getDepartmentPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "departmentType", defaultValue = "", required = false) String departmentType,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords
    ) {
        DepartmentQuery query = DepartmentQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .departmentType(departmentType)
                .keywords(keywords)
                .build();
        return departmentQueryHandler.handleQueryPage(query);
    }
}
