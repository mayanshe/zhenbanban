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
import com.zhenbanban.core.domain.internethospitalcontext.valueobj.HospitalLevel;
import com.zhenbanban.core.domain.internethospitalcontext.valueobj.HospitalOwnershipType;
import com.zhenbanban.core.domain.internethospitalcontext.valueobj.HospitalType;
import com.zhenbanban.core.infrastructure.persistence.mapper.HospitalPoMapper;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.infrastructure.util.CollectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 控制器 : 业务医院
 *
 * @author zhangxihai 2025/09/17
 */
@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalAmdCmdHandler hospitalAmdCmdHandler;
    private final HospitalQueryHandler hospitalQueryHandler;
    private final HospitalPoMapper mapper;

    @Autowired
    public HospitalController(
            @Lazy HospitalAmdCmdHandler hospitalAmdCmdHandler,
            @Lazy HospitalQueryHandler hospitalQueryHandler,
            @Lazy HospitalPoMapper mapper
    ) {
        this.hospitalAmdCmdHandler = hospitalAmdCmdHandler;
        this.hospitalQueryHandler = hospitalQueryHandler;
        this.mapper = mapper;
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
        HospitalAmdCommand command = HospitalAmdCommand.builder()
                .ownershipType(request.getOwnershipType())
                .hospitalType(request.getHospitalType())
                .hospitalLevel(request.getHospitalLevel())
                .insuranceCode(request.getInsuranceCode())
                .usccCode(request.getUsccCode())
                .hospitalCode(request.getHospitalCode())
                .hospitalName(request.getHospitalName())
                .hospitalNamePinyin(request.getHospitalName() != null ? request.getHospitalName().trim() : "")
                .hospitalNamePinyinAbbr(request.getHospitalName() != null ? request.getHospitalName().trim() : "")
                .provinceId(request.getProvinceId())
                .province(request.getProvince())
                .cityId(request.getCityId())
                .city(request.getCity())
                .countyId(request.getCountyId())
                .county(request.getCounty())
                .address(request.getAddress())
                .postalCode(request.getPostalCode())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .mapUrl(request.getMapUrl())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .website(request.getWebsite())
                .summary(request.getSummary())
                .description(request.getDescription())
                .companionDiagnosisEnabled(request.getCompanionDiagnosisEnabled() != null && request.getCompanionDiagnosisEnabled())
                .mealServiceEnabled(request.getMealServiceEnabled() != null && request.getMealServiceEnabled())
                .testingDeliveryEnabled(request.getTestingDeliveryEnabled() != null && request.getTestingDeliveryEnabled())
                .build();
        setRegionNames(command);

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
        HospitalAmdCommand command = HospitalAmdCommand.builder()
                .ownershipType(request.getOwnershipType())
                .hospitalType(request.getHospitalType())
                .hospitalLevel(request.getHospitalLevel())
                .insuranceCode(request.getInsuranceCode())
                .usccCode(request.getUsccCode())
                .hospitalCode(request.getHospitalCode())
                .hospitalName(request.getHospitalName())
                .hospitalNamePinyin(request.getHospitalName() != null ? request.getHospitalName().trim() : "")
                .hospitalNamePinyinAbbr(request.getHospitalName() != null ? request.getHospitalName().trim() : "")
                .provinceId(request.getProvinceId())
                .province(request.getProvince())
                .cityId(request.getCityId())
                .city(request.getCity())
                .countyId(request.getCountyId())
                .county(request.getCounty())
                .address(request.getAddress())
                .postalCode(request.getPostalCode())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .mapUrl(request.getMapUrl())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .website(request.getWebsite())
                .summary(request.getSummary())
                .description(request.getDescription())
                .companionDiagnosisEnabled(request.getCompanionDiagnosisEnabled() != null && request.getCompanionDiagnosisEnabled())
                .mealServiceEnabled(request.getMealServiceEnabled() != null && request.getMealServiceEnabled())
                .testingDeliveryEnabled(request.getTestingDeliveryEnabled() != null && request.getTestingDeliveryEnabled())
                .build();
        command.setId(id);
        setRegionNames(command);

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

    /**
     * 获取医院级别列表
     *
     * @return 列表
     */
    @GetMapping("/levels")
    public List<HospitalLevel> getHospitalLevels() {
        return HospitalLevel.all();
    }

    /**
     * 获取医院类型列表
     *
     * @return 列表
     */
    @GetMapping("/types")
    public List<HospitalType> getHospitalTypes() {
        return HospitalType.all();
    }

    /**
     * 获取医院所有制类型列表
     *
     * @return 列表
     */
    @GetMapping("/ownership-types")
    public List<HospitalOwnershipType> getHospitalOwnershipTypes() {
        return HospitalOwnershipType.all();
    }

    private void setRegionNames(HospitalAmdCommand command) {
        Set<Long> p = new HashSet<>();
        p.add(command.getProvinceId());
        p.add(command.getCityId());
        p.add(command.getCountyId());
        Map<Long, Map<String, Object>> regions = CollectUtil.convertListToMap(mapper.findNameMapByIds(p), "id");

        if (regions.containsKey(command.getProvinceId())) {
            command.setProvince((String) regions.get(command.getProvinceId()).get("region_name"));
        }

        if (regions.containsKey(command.getCityId())) {
            command.setCity((String) regions.get(command.getCityId()).get("region_name"));
        }

        if (regions.containsKey(command.getCountyId())) {
            command.setCounty((String) regions.get(command.getCountyId()).get("region_name"));
        }
    }

}
