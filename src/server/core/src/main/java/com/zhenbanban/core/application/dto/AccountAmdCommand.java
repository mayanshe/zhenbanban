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

import com.zhenbanban.core.application.common.BaseCommand;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 命令载体 : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountAmdCommand extends BaseCommand<Long> {
    private String username;              // 用户名，唯一

    private String password;              // 密码，至少8个字符

    @Builder.Default
    private String surname = "";          // 姓氏

    @Builder.Default
    private String givenName = "";        // 名字

    @Builder.Default
    private short gender = 0;             // 性别，0-未知，1-男，2-女

    @Builder.Default
    private String email = "";            // 邮箱地址，唯一

    private String phone;                 // 手机号码，唯一

    @Builder.Default
    private String avatar = "";           // 头像URL，默认为空字符串

}
