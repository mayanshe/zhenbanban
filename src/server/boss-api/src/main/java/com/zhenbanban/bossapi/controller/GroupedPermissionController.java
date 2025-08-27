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

import com.zhenbanban.core.application.dto.GroupedPermissionDto;
import com.zhenbanban.core.application.query.GroupedPermissionQueryHandler;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller : 分组权限
 *
 * @author zhangxihai 2025/8/20
 */
@RestController
@RequestMapping("/grouped-permissions")
public class GroupedPermissionController {
    private final GroupedPermissionQueryHandler groupedPermissionQueryHandler;

    public GroupedPermissionController(@Lazy GroupedPermissionQueryHandler groupedPermissionQueryHandler) {
        this.groupedPermissionQueryHandler = groupedPermissionQueryHandler;
    }

    @GetMapping
    @AdminPermit(permissions = {"role:assignment:modify"})
    public List<GroupedPermissionDto> getGroupedPermissionController() {
        return groupedPermissionQueryHandler.handle();
    }

}

