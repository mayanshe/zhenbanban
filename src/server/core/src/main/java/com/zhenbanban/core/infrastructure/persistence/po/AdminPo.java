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
import org.apache.ibatis.annotations.One;

import java.util.Set;

/**
 * Po: 系统管理员
 *
 * @author zhangxihai 2025/08/01
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminPo {

    private Long id;                                // 账户ID

    private String username;                        // 管理员用户名

    private String surname;                         // 管理员姓

    private String givenName;                       // 管理员名

    private String password;                        // 管理员密码，存储加密后的密码

    private String email;                           // 管理员邮箱

    private String phone;                           // 管理员手机号码

    private String avatar;                          // 管理员头像URL

    @Builder.Default
    private String lastLoginIp = "";                // 最后登录IP

    @Builder.Default
    private long lastLoginAt = 0L;                  // 最后登录时间

    @Builder.Default
    private long createdAt = 0L;                      // 创建时间

    @Builder.Default
    private long updatedAt = 0L;                     // 更新时间

    @Builder.Default
    private long deletedAt = 0L;                     // 删除时间（逻辑删除）

    @Builder.Default
    private Set<RolePo> roles = Set.of();            // 管理员角色集合，使用Set避免重复角色

}
