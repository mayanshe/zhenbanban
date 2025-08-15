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
package com.zhenbanban.core.application.query.impl;

import com.zhenbanban.core.application.dto.AdminStateView;
import com.zhenbanban.core.application.query.AdminProfileQueryHandler;
import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.shared.contract.IAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 查询实现 : 管理员Profile
 *
 * @author zhangxihai 2025/8/11
 */
@Service
@RequiredArgsConstructor
public class AdminProfileQueryHandlerImpl implements AdminProfileQueryHandler {
    private final IAuth<Admin> auth;

    public AdminStateView handle() {
        Admin admin = auth.user();

        return AdminStateView.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .surname(admin.getSurname())
                .givenName(admin.getGivenName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .avatar(admin.getAvatar())
                .build();
    }

}
