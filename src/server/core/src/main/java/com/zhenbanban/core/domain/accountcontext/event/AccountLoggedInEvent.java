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
package com.zhenbanban.core.domain.accountcontext.event;

import com.zhenbanban.core.domain.common.AbsDomainEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Event : 账号登陆成功事件
 *
 * @author zhangxihai 2025/8/4
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoggedInEvent extends AbsDomainEvent {
    private String scope;                  // 作用域，表示登录的范围，如"admin"、"user"等

    private String kind;                   // 登录方式

    private String platform;               // 平台，表示登录的设备或平台，如"web"、"mobile"等

    private Long accountId;                // 账号

    private String username;               // 账号用户名

    private String clientIp;               // 客户端IP地址

    private String userAgent;              // 用户代理信息，包含浏览器、操作系统等信息

    private String browserFingerprint;     // 浏览器指纹，用于唯一标识浏览器实例

    private String tokenMd5;               // 登录凭证的MD5值，用于验证登录状态

    public void setScope(String scope) {
        this.scope = scope == null ? "" : scope.toLowerCase();
    }

    public void setKind(String kind) {
        this.kind = kind == null ? "" : kind.toLowerCase();
    }

}
