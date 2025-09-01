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

import com.zhenbanban.bossapi.vo.MedicineSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.MedicineAmdCmdHandler;
import com.zhenbanban.core.application.dto.MedicineAmdCommand;
import com.zhenbanban.core.application.dto.MedicineDto;
import com.zhenbanban.core.application.dto.MedicineQuery;
import com.zhenbanban.core.application.query.MedicineQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * 控制器 : 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@RestController
@RequestMapping("/medicines")
public class MedicineController {
    private final MedicineAmdCmdHandler medicineAmdCmdHandler;
    private final MedicineQueryHandler medicineQueryHandler;

    @Autowired
    public MedicineController(
            @Lazy MedicineAmdCmdHandler medicineAmdCmdHandler,
            @Lazy MedicineQueryHandler medicineQueryHandler
    ) {
        this.medicineAmdCmdHandler = medicineAmdCmdHandler;
        this.medicineQueryHandler = medicineQueryHandler;
    }

    /**
     * 添加西药及中成药
     *
     * @param request 药品信息
     * @return 药品ID
     */
    @PostMapping
    @AdminPermit(permissions = {"medicine:add"}, message = "您未被授权执行此操作：添加西药/中成药")
    public IdResponse addMedicine(@Valid @RequestBody MedicineSaveRequest request) {
        MedicineAmdCommand command = (new ModelMapper()).map(request, MedicineAmdCommand.class);
        Long medicineId = medicineAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(medicineId).build();
    }

    /**
     * 更新西药及中成药
     *
     * @param request 药品信息
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"medicine:modify"}, message = "您未被授权执行此操作：修改西药/中成药信息")
    public void modifyMedicine(@PathVariable("id") Long id, @Valid @RequestBody MedicineSaveRequest request) {
        MedicineAmdCommand command = (new ModelMapper()).map(request, MedicineAmdCommand.class);
        command.setId(id);
        medicineAmdCmdHandler.handleModify(command);
    }

    /**
     * 删除西药及中成药
     *
     * @param id 药品ID
     */
    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"medicine:delete"}, message = "您未被授权执行此操作：删除西药/中成药")
    public void deleteMedicine(@PathVariable("id") Long id) {
        medicineAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取西药及中成药
     *
     * @param id 药品ID
     * @return 药品信息
     */
    @GetMapping("/{id}")
    @AdminPermit(permissions = {"medicine:add", "medicine:modify", "medicine:delete"}, message = "您未被授权执行此操作：查询西药/中成药信息")
    public MedicineDto getMedicine(@PathVariable("id") Long id) {
        return medicineQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取西药及中成药分页列表
     *
     * @param page     当前页
     * @param pageSize 页码
     * @param keywords 关键词
     * @return 药品分页信息
     */
    @GetMapping
    @AdminPermit(permissions = {"medicine:add", "medicine:modify", "medicine:delete"}, message = "您未被授权执行此操作：查询西药/中成药分页")
    public Pagination<MedicineDto> getMedicinePagination(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize,
            @RequestParam(value = "keywords", defaultValue = "", required = false) String keywords,
            @RequestParam(value = "medicineCode", defaultValue = "", required = false) String medicineCode,
            @RequestParam(value = "deleted", defaultValue = "false", required = false) boolean deleted,
            @RequestParam(value = "icd",  required = false) boolean icd,
            @RequestParam(value = "poisonous",  required = false) boolean poisonous
    ) {
        MedicineQuery query = MedicineQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .keywords(keywords)
                .medicineCode(medicineCode)
                .deleted(deleted)
                .otc(icd)
                .poisonous(poisonous)
                .build();

        return medicineQueryHandler.handleQueryPage(query);
    }

}
