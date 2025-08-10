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
package com.zhenbanban.core.infrastructure.persistence.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Po: 账户
 *
 * @author zhangxihai 2025/08/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountPo {
    private Long id;                     // 账户ID

    @Builder.Default
    private String scopes = "user";      // 权限范围（逗号分隔）

    private String username;             // 用户名

    @Builder.Default
    private String surname = "";         // 姓

    @Builder.Default
    private String givenName = "";       // 名

    @Builder.Default
    private Short gender = 0;            // 性别: 0-未知, 1-男, 2-女

    private String email;                // 邮箱地址

    private String phone;                // 手机号码

    private String password;             // 密码（加密存储）

    @Builder.Default
    private String avatar = "";          // 头像URL

    @Builder.Default
    private short state = 10;            // 账户状态: 0-仅用, 10-激活

    @Builder.Default
    private Long version = 0L;           // 乐观锁版本号

    @Builder.Default
    private long createdAt = 0L;         // 创建时间戳

    @Builder.Default
    private long updatedAt = 0L;         // 更新时间戳

}
