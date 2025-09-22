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

import com.zhenbanban.core.domain.common.valueobj.AuditStatus;
import com.zhenbanban.core.domain.common.valueobj.WithAuditStatus;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller : 通用
 *
 * @author zhangxihai 2025/9/18
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    /**
     * 获取带审核状态列表
     *
     * @return 列表
     */
    @GetMapping("/with-audit-statuses")
    public List<WithAuditStatus> getWithAuditStatusList() {
        return WithAuditStatus.all();
    }

    /**
     * 获取审核状态列表
     *
     * @return 列表
     */
    @GetMapping("/audit-statuses")
    public List<AuditStatus> getAuditStatusList() {
        return AuditStatus.all();
    }

}
