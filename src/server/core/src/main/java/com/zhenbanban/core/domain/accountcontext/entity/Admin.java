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

import com.zhenbanban.core.domain.accountcontext.event.*;
import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.infrastructure.util.BCryptUtils;
import com.zhenbanban.core.infrastructure.util.StrUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.*;

import java.util.Set;

/**
 * 聚合根：管理员
 *
 * @author zhangxihai 2025/08/01
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends AbsAggregate {
    private Long id;                                // 管理员ID

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
    private Long lastLoginAt = 0L;                   // 最后登录时间戳，单位毫秒

    @Builder.Default
    private boolean deleted = false;                // 是否已删除

    @Builder.Default
    private boolean roleChanged = false;            // 角色是否已变更

    @Builder.Default
    private Set<Long> roleIds = Set.of();           // 管理员角色ID集合

    @Builder.Default
    private Set<String> roleNames = Set.of();       // 管理员角色名称集合

    private Set<String> permissionNames = Set.of(); // 管理员权限名称集合

    private boolean superAdmin = false;             // 是否为系统管理员

    /**
     * 判断是否为超级管理员
     *
     * @return 如果是超级管理员，返回true；否则返回false
     */
    public boolean isSuperAdmin() {
        return roleNames.contains("administrator");
    }

    /**
     * 添加管理员
     */
    public void add() {
        this.setDeleted(false);

        AdminAddedEvent event = AdminAddedEvent.builder()
                .refId(this.getId())
                .createdBy("current")
                .adminId(this.id)
                .build();

        this.addEvent(event);
    }

    /**
     * 更新管理员信息
     */
    public void modify() {
        this.setDeleted(false);

        AdminModifiedEvent event = AdminModifiedEvent.builder()
                .refId(this.getId())
                .createdBy("current")
                .adminId(this.id)
                .build();

        this.addEvent(event);
    }

    /**
     * 禁用管理员
     */
    public void destroy() {
        this.setDeleted(true);

        AdminDestroyedEvent event = AdminDestroyedEvent.builder()
                .refId(this.getId())
                .adminId(this.id)
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 激活管理员
     */
    public void activate() {
        this.setDeleted(false);

        AdminModifiedEvent event = AdminModifiedEvent.builder()
                .refId(this.getId())
                .adminId(this.id)
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /*
     * 绑定角色
     */
    public void bindingRoles(Set<Long> roleIds) {
        this.setRoleChanged(true);
        this.setDeleted(false);
        this.setRoleIds(roleIds);

        AdminRolesBoundEvent event = AdminRolesBoundEvent.builder()
                .refId(this.getId())
                .adminId(this.getId())
                .roleIds(roleIds)
                .oldRoleIds(this.getRoleIds())
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /* * 管理员登录
     *
     * @param clientIp 客户端IP地址
     * @param userAgent 用户代理信息
     * @param browserFingerprint 浏览器指纹信息
     */
    public void login(String scope, String kind, String Platform, String clientIp, String userAgent, String browserFingerprint, String token) {
        if (this.isDeleted()) {
            throw new BadRequestException("管理员账号已被禁用，无法登录。");
        }

        AccountLoggedInEvent event = AccountLoggedInEvent.builder()
                .refId(this.getId())
                .scope(scope)
                .kind(kind)
                .accountId(this.getId())
                .username(this.getUsername())
                .platform(Platform)
                .clientIp(clientIp)
                .userAgent(userAgent)
                .browserFingerprint(browserFingerprint)
                .tokenMd5(StrUtils.convertToMd5(token))
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    public void logout(String token) {
        AccountLoggedOutEvent event = AccountLoggedOutEvent.builder()
                .refId(this.getId())
                .accountId(this.getId())
                .username(this.getUsername())
                .tokenMd5(StrUtils.convertToMd5(token))
                .build();
        event.setCreatedBy("current");

        this.addEvent(event);
    }

    /**
     * 设置管理员密码
     *
     * @param plainPassword 明文密码
     */
    public boolean verifyPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return false;
        }

        return BCryptUtils.verify(plainPassword, this.password);
    }

}
