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

import com.zhenbanban.core.application.dto.AdminDto;
import com.zhenbanban.core.application.query.AdminProfileQuery;
import com.zhenbanban.core.infrastructure.support.annotation.AdminPermit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器 : 我的
 *
 * @author zhangxihai 2025/8/11
 */
@RestController
@RequestMapping("/my")
public class MyController {
    private final AdminProfileQuery adminProfileQuery;

    @Autowired
    public MyController(@Lazy AdminProfileQuery adminProfileQuery) {
        this.adminProfileQuery = adminProfileQuery;
    }

    /**
     * 获取管理员个人信息
     *
     * @return 管理员个人信息
     */
    @GetMapping("/profile")
    @AdminPermit
    public AdminDto getProfile() {
        return adminProfileQuery.handle();
    }

}
