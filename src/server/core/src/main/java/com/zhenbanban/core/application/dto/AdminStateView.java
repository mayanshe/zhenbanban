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
package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.infrastructure.util.StrUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto : 管理员
 *
 * @author zhangxihai 2025/8/11
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminStateView {
    private Long id;            // 管理员ID

    private String username;     // 用户名

    private String surname;      // 姓

    private String givenName;    // 名

    private String email;        // 邮箱

    private String phone;        // 手机号

    private String avatar;       // 头像URL

    /**
     * 获取脱敏后的邮箱地址
     *
     * @return 脱敏后的邮箱地址
     */
    public String getEmail() {
        return email == null ? "" : StrUtils.desensitizeEmail(email);
    }

    /**
     * 获取脱敏后的手机号码
     *
     * @return 脱敏后的手机号码
     */
    public String getPhone() {
        return phone == null ? "" : StrUtils.desensitizePhoneNumber(phone);
    }

}
