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
package com.zhenbanban.core.domain.accountcontext.entity;

import com.zhenbanban.core.domain.accountcontext.event.AccountActivatedEvent;
import com.zhenbanban.core.domain.accountcontext.event.AccountAddedEvent;
import com.zhenbanban.core.domain.accountcontext.valueobj.AccountScope;
import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.infrastructure.util.BCryptUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.*;

import java.util.Set;

/**
 * Aggregate : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbsAggregate {
    private Long id;                                  // 账号ID，唯一

    @Builder.Default
    private Set<String> scopes = Set.of("user");      // 账号权限范围，默认为空集合

    private String username;                          // 用户名，唯一

    @Builder.Default
    private String surname = "";                      // 姓，默认为空字符串

    @Builder.Default
    private String givenName = "";                    // 名，默认为空字符串

    @Builder.Default
    private short gender = 0;                         // 性别，0-未知, 1-男, 2-女，默认为0

    private String password;                          // 密码，至少8个字符

    @Builder.Default
    private String email = "";                         // 邮箱地址，唯一

    private String phone;                              // 手机号码，唯一

    @Builder.Default
    private String avatar = "";                        // 头像URL，默认为空字符串

    @Builder.Default
    private boolean forbidden = false;                 // 是否禁用, 默认为否

    @Builder.Default
    private Long version = 0L;                         // 乐观锁版本号，用于并发控制

    /**
     * 添加权限范围
     *
     * @param scope 权限范围字符串
     */
    public void addScope(String scope) {
        if (scope != null && !scope.isEmpty()) {
            scope = scope.trim().toLowerCase();
            AccountScope.of(scope); // 验证scope是否有效
            this.scopes.remove(scope);
            this.scopes.add(scope);
        } else {
            throw new IllegalArgumentException("Scope cannot be null or empty.");
        }
    }

    /**
     * 移除权限范围
     *
     * @param scope 权限范围字符串
     */
    public void removeScope(String scope) {
        if (scope != null && !scope.isEmpty()) {
            this.scopes.remove(scope);
        } else {
            throw new IllegalArgumentException("Scope cannot be null or empty.");
        }
    }

    /**
     * 检查是否具有指定的权限范围
     *
     * @param scope 权限范围字符串
     * @return 如果包含指定的权限范围返回true，否则返回false
     */
    public boolean hasScope(String scope) {
        return this.scopes.contains(scope);
    }

    /**
     * 设置账号密码
     *
     * @param password 原始密码
     */
    public void setPassword(String password) {
        this.password = password == null || password.isEmpty() ? "" : BCryptUtils.hash(password.trim());
    }

    /**
     * 验证密码是否正确
     *
     * @param plainPassword 明文密码
     * @return 如果密码匹配返回true，否则返回false
     */
    public boolean verifyPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return false;
        }

        return BCryptUtils.verify(plainPassword, this.password);
    }

    /**
     * 添加账号
     */
    public void add() {
        this.setForbidden(false);

        if (this.getPassword() == null || this.getPassword().isEmpty()) {
            throw new BadRequestException("密码不能为空，请设置一个有效的密码。");
        }

        AccountAddedEvent event = AccountAddedEvent.builder()
                .refId(this.getId())
                .accountId(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .phone(this.getPhone())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 更新账号
     */
    public void modify() {
        if (this.isForbidden()) {
            throw new BadRequestException("无法修改被禁用的账号，请先启用账号后再进行修改。");
        }

        AccountAddedEvent event = AccountAddedEvent.builder()
                .refId(this.getId())
                .accountId(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .phone(this.getPhone())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 禁用账号
     */
    public void destroy() {
        if (this.isForbidden()) {
            throw new BadRequestException("账号已被禁用，无法再次禁用。");
        }

        this.setForbidden(true);

        AccountAddedEvent event = AccountAddedEvent.builder()
                .refId(this.getId())
                .accountId(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .phone(this.getPhone())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 激活账号
     */
    public void activate() {
        if (!this.isForbidden()) {
            throw new BadRequestException("账号未被禁用，无法激活。");
        }

        this.setForbidden(false);

        AccountActivatedEvent event = AccountActivatedEvent.builder()
                .refId(this.getId())
                .accountId(this.getId())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

}
