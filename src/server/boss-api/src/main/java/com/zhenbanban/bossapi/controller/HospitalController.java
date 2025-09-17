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

import com.zhenbanban.bossapi.vo.HospitalSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.HospitalAmdCmdHandler;
import com.zhenbanban.core.application.dto.HospitalAmdCommand;
import com.zhenbanban.core.application.dto.HospitalDto;
import com.zhenbanban.core.application.dto.HospitalQuery;
import com.zhenbanban.core.application.query.HospitalQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 业务医院表
 *
 * @author zhangxihai 2025/09/17
 */
@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalAmdCmdHandler hospitalAmdCmdHandler;
    private final HospitalQueryHandler hospitalQueryHandler;

    @Autowired
    public HospitalController(
            @Lazy HospitalAmdCmdHandler hospitalAmdCmdHandler,
            @Lazy HospitalQueryHandler hospitalQueryHandler
    ) {
        this.hospitalAmdCmdHandler = hospitalAmdCmdHandler;
        this.hospitalQueryHandler = hospitalQueryHandler;
    }

    /**
     * 添加业务医院表
     *
     * @param request 业务医院表信息
     * @return 业务医院表ID
     */
    @PostMapping
    @AdminPermit(permissions = {"hospital:add"}, message = "您未被授权执行此操作：添加业务医院表")
    public IdResponse addHospital(@Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = (new ModelMapper()).map(request, HospitalAmdCommand.class);
        Long hospitalId = hospitalAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(hospitalId).build();
    }

    /**
     * 更新业务医院表
     *
     * @param request 业务医院表信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"hospital:modify"}, message = "您未被授权执行此操作：修改业务医院表信息")
    public void modifyHospital(@PathVariable("id") Long id, @Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = (new ModelMapper()).map(request, HospitalAmdCommand.class);
        command.setId(id);
        hospitalAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除业务医院表
     *
     * @param id 业务医院表ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"hospital:delete"}, message = "您未被授权执行此操作：删除业务医院表")
    public void deleteHospital(@PathVariable("id") Long id) {
        hospitalAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取业务医院表
     *
     * @param id 业务医院表ID
     * @return 业务医院表信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"hospital:add", "hospital:modify", "hospital:delete"}, message = "您未被授权执行此操作：查询业务医院表")
    public HospitalDto getHospital(@PathVariable("id") Long id) {
        return hospitalQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取业务医院表分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param keywords 关键词
     * @return 业务医院表分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"hospital:add", "hospital:modify", "hospital:delete"}, message = "您未被授权执行此操作：查询业务医院表")
    public Pagination<HospitalDto> getHospitalPagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "hospitalCode", defaultValue = "", required = false) String hospitalCode,
            @RequestParam(value = "deleted", defaultValue = "false", required = false) boolean deleted
    ) {
        HospitalQuery query = HospitalQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .hospitalCode(hospitalCode)
                .deleted(deleted)
                .build();

        return hospitalQueryHandler.handleQueryPage(query);
    }
}
