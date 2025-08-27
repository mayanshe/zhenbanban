package com.zhenbanban.bossapi.controller;

import com.zhenbanban.bossapi.vo.HospitalSaveRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.core.application.command.HospitalAmdCmdHandler;
import com.zhenbanban.core.application.dto.HospitalAmdCommand;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalAmdCmdHandler hospitalAmdCmdHandler;

    public HospitalController(@Lazy HospitalAmdCmdHandler hospitalAmdCmdHandler) {
        this.hospitalAmdCmdHandler = hospitalAmdCmdHandler;
    }

    /**
     * 添加医院
     *
     * @param request 医院保存请求
     * @return IdResponse 包含新创建医院的ID
     */
    @PostMapping
    @AdminPermit(permissions = {"hospital:add"}, message = "您未被授权执行此操作：添加医院")
    public IdResponse addHospital(@Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = new ModelMapper().map(request, HospitalAmdCommand.class);
        Long hospitalId = hospitalAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(hospitalId).build();
    }

    /**
     * 修改医院信息
     *
     * @param id      医院ID
     * @param request 医院保存请求
     */
    @PutMapping("/{id}")
    @AdminPermit(permissions = {"hospital:modify"}, message = "您未被授权执行此操作：修改医院信息")
    public void modifyHospital(@PathVariable("id") Long id, @Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = new ModelMapper().map(request, HospitalAmdCommand.class);
        command.setId(id);
        hospitalAmdCmdHandler.handleModify(command);
    }

    /**
     * 禁用医院
     *
     * @param id 医院ID
     */
    @PostMapping("/{id}/lock")
    @AdminPermit(permissions = {"hospital:delete"}, message = "您未被授权执行此操作：禁用医院")
    public void destroyHospital(@PathVariable("id") Long id) {
        hospitalAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 启用医院
     *
     * @param id 医院ID
     */
    @DeleteMapping("/{id}/lock")
    @AdminPermit(permissions = {"hospital:activate"}, message = "您未被授权执行此操作：禁用医院")
    public void activateHospital(@PathVariable("id") Long id) {
        hospitalAmdCmdHandler.handleActivate(id);
    }

}
