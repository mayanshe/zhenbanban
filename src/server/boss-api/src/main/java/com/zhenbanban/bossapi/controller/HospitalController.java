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

    @PostMapping
    @AdminPermit(permissions = {"hospital:add"}, message = "您未被授权执行此操作：添加医院")
    public IdResponse addHospital(@Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = new ModelMapper().map(request, HospitalAmdCommand.class);
        Long hospitalId = hospitalAmdCmdHandler.handleAdd(command);
        return IdResponse.builder().id(hospitalId).build();
    }

    @PutMapping("/{id}")
    @AdminPermit(permissions = {"hospital:modify"}, message = "您未被授权执行此操作：修改医院信息")
    public void modifyHospital(@PathVariable("id") Long id, @Valid @RequestBody HospitalSaveRequest request) {
        HospitalAmdCommand command = new ModelMapper().map(request, HospitalAmdCommand.class);
        command.setId(id);
        hospitalAmdCmdHandler.handleModify(command);
    }

    @DeleteMapping("/{id}")
    @AdminPermit(permissions = {"hospital:delete"}, message = "您未被授权执行此操作：删除医院")
    public void destroyHospital(@PathVariable("id") Long id) {
        hospitalAmdCmdHandler.handleDestroy(id);
    }
}
