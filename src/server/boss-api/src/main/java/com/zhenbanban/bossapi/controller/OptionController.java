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

import com.zhenbanban.bossapi.vo.CosSettingRequest;
import com.zhenbanban.bossapi.vo.IdResponse;
import com.zhenbanban.bossapi.vo.OptionSaveRequest;
import com.zhenbanban.bossapi.vo.SiteInfoRequest;
import com.zhenbanban.core.application.command.OptionAmdCmdHandler;
import com.zhenbanban.core.application.dto.CustomizedOptionQuery;
import com.zhenbanban.core.application.dto.OptionAmdCommand;
import com.zhenbanban.core.application.dto.OptionDto;
import com.zhenbanban.core.application.query.CustomizedOptionQueryHandler;
import com.zhenbanban.core.application.query.FixedOptionQueryHandler;
import com.zhenbanban.core.infrastructure.external.cos.CosSetting;
import com.zhenbanban.core.domain.systemcontext.valueobj.SiteInfo;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.BadRequestException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * Controller : 系统配置选项
 *
 * @author zhangxihai 2025/8/24
 */
@RestController
@RequestMapping("options")
public class OptionController {
    private final OptionAmdCmdHandler optionAmdCmdHandler;

    private final CustomizedOptionQueryHandler customizedOptionQueryHandler;

    private final FixedOptionQueryHandler fixedOptionQueryHandler;

    @Autowired
    public OptionController(
            @Lazy OptionAmdCmdHandler optionAmdCmdHandler,
            @Lazy CustomizedOptionQueryHandler customizedOptionQueryHandler,
            @Lazy FixedOptionQueryHandler fixedOptionQueryHandler
    ) {
        this.optionAmdCmdHandler = optionAmdCmdHandler;
        this.customizedOptionQueryHandler = customizedOptionQueryHandler;
        this.fixedOptionQueryHandler = fixedOptionQueryHandler;
    }

    /**
     * 创建系统自定义配置选项
     *
     * @param request 请求参数
     * @return IdResponse
     */
    @PostMapping
    @AdminPermit(permissions = {"option:add"}, message = "您未被授权执行此操作：添加自定义配置")
    public IdResponse createOption(@Valid @RequestBody OptionSaveRequest request) {
        OptionAmdCommand command = OptionAmdCommand.builder()
                .optionName(request.getOptionName())
                .displayName(request.getDisplayName())
                .optionValue(request.getOptionValue())
                .description(request.getDescription())
                .customized(true)
                .build();
        return IdResponse.builder()
                .id(optionAmdCmdHandler.handleAdd(command))
                .build();
    }

    /**
     * 更新系统自定义配置选项
     *
     * @param id      配置选项ID
     * @param request 请求参数
     */
    @PutMapping("/{id:\\d+}")
    @AdminPermit(permissions = {"option:modify"}, message = "您未被授权执行此操作：修改自定义配置")
    public void updateOption(@PathVariable("id") Long id, @Valid @RequestBody OptionSaveRequest request) {
        OptionAmdCommand command = OptionAmdCommand.builder()
                .id(id)
                .optionName(request.getOptionName())
                .displayName(request.getDisplayName())
                .optionValue(request.getOptionValue())
                .description(request.getDescription())
                .customized(true)
                .build();
        optionAmdCmdHandler.handleModify(command);
    }

    @DeleteMapping("/{id:\\d+}")
    @AdminPermit(permissions = {"option:delete"}, message = "您未被授权执行此操作：删除自定义配置")
    public void deleteOption(@PathVariable("id") Long id) {
        optionAmdCmdHandler.handleDestroy(id);
    }

    /**
     * 获取系统自定义配置选项
     *
     * @param id 配置选项ID
     * @return OptionDto
     */
    @GetMapping("/{id:\\d+}")
    @AdminPermit(permissions = {"option:add", "option:modify", "option:delete"}, message = "您未被授权执行此操作：查询自定义配置")
    public OptionDto getOption(@PathVariable("id") Long id) {
        return customizedOptionQueryHandler.handleQuerySingle(id);
    }

    /**
     * 获取系统自定义配置选项列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param id       配置选项ID
     * @param keywords 关键词
     * @return Pagination<OptionDto>
     */
    @GetMapping
    @AdminPermit(permissions = {"option:add", "option:modify", "option:delete"}, message = "您未被授权执行此操作：查询自定义配置列表")
    public Pagination<OptionDto> getOptionPagination(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "keywords", required = false, defaultValue = "") String keywords
    ) {
        CustomizedOptionQuery query = CustomizedOptionQuery.builder()
                .page(page)
                .pageSize(pageSize)
                .id(id)
                .keywords(keywords)
                .build();

        return customizedOptionQueryHandler.handleQueryPage(query);
    }

    /**
     * 修改站点信息
     *
     * @param request 站点信息请求参数
     */
    @PutMapping("/site-info")
    @AdminPermit(permissions = {"fixed-option:modify"}, message = "您未被授权执行此操作：修改站点信息")
    public void modifySiteInfo(@Valid @RequestBody SiteInfoRequest request) {
        SiteInfo siteInfo = (new ModelMapper()).map(request, SiteInfo.class);

        String optionValue;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(siteInfo);
            optionValue = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new BadRequestException("保存站点信息失败");
        }

        OptionAmdCommand command = OptionAmdCommand.builder()
                .optionName("site-info")
                .displayName("站点信息")
                .optionValue(optionValue)
                .description("站点信息配置")
                .customized(false)
                .build();
        optionAmdCmdHandler.handleModifyFixedOption(command);
    }

    /**
     * 获取站点信息
     *
     * @return SiteInfo
     */
    @GetMapping("/site-info")
    public SiteInfo getSiteInfo() {
        return fixedOptionQueryHandler.handle("site-info", "站点信息", SiteInfo.class, SiteInfo.defaults());
    }

    /**
     * 修改COS对象存储配置
     *
     * @param request COS配置请求参数
     */
    @PutMapping("/cos-setting")
    @AdminPermit(permissions = {"fixed-option:modify"}, message = "您未被授权执行此操作：修改COS对象存储配置")
    public void modifyCosConfig(@Valid @RequestBody CosSettingRequest request) {
        CosSetting cosConfig = (new ModelMapper()).map(request, CosSetting.class);

        String optionValue;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(cosConfig);
            optionValue = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new BadRequestException("保存COS对象存储配置失败");
        }

        OptionAmdCommand command = OptionAmdCommand.builder()
                .optionName("cos-setting")
                .displayName("COS配置")
                .optionValue(optionValue)
                .description("COS配置")
                .customized(false)
                .build();
        optionAmdCmdHandler.handleModifyFixedOption(command);
    }

    /**
     * 获取COS对象存储配置
     *
     * @return CosSetting
     */
    @GetMapping("/cos-setting")
    @AdminPermit(permissions = {"fixed-option:modify"}, message = "您未被授权执行此操作：查询COS对象存储配置")
    public CosSetting getCosConfig() {
        return fixedOptionQueryHandler.handle("cos-setting", "COS配置", CosSetting.class, CosSetting.defaults());
    }

}
