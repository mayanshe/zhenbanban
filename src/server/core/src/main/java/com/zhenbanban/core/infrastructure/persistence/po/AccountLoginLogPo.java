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

/**
 * Po : 账号登陆日志
 *
 * @author zhangxihai 2025/8/7
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginLogPo {
    private Long id;                           // 日志ID

    private Long accountId;                    // 账户ID

    @Builder.Default
    private String username = "";              // 用户名

    @Builder.Default
    private String scope = "";                 // 权限范围（逗号分隔）

    @Builder.Default
    private String kind = "";                  // 登陆类型（如：web, mobile, api）

    @Builder.Default
    private String platform = "";              // 登陆平台（如：Windows, macOS, Linux, Android, iOS）

    @Builder.Default
    private String clientIp = "";              // 客户端IP地址

    @Builder.Default
    private String userAgent = "";             // 用户代理字符串

    @Builder.Default
    private String browserFingerprint = "";    // 浏览器指纹信息

    @Builder.Default
    private String tokenMd5 = "";              // Token的MD5值，用于验证Token的完整性

    @Builder.Default
    private Long loginAt = 0L;                 // 登陆时间戳，0表示未登陆

    @Builder.Default
    private Long logoutAt = 0L;                // 登出时间戳，0表示未登出

}
