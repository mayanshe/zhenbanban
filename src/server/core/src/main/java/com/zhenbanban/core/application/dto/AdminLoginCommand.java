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

import lombok.*;

/**
 * 类型 : AdminLoginCommand
 *
 * @author zhangxihai 2025/8/4
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginCommand {
    private String account;                         // 用户名

    private String password;                        // 密码

    @Builder.Default
    private boolean rememberMe = false;             // 是否记住我

    @Builder.Default
    private String clientIp = "";                   // 客户端IP地址，用于安全审计和登录日志记录

    @Builder.Default
    private String userAgent = "";                  // 用户代理信息，用于安全审计和登录日志记录

    @Builder.Default
    private String browserFingerprint = "";         // 浏览器指纹信息，用于安全审计和登录日志记录

}
