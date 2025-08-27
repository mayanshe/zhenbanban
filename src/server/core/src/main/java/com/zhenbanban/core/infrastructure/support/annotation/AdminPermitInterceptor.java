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
package com.zhenbanban.core.infrastructure.support.annotation;

import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.shared.contract.IAuth;
import com.zhenbanban.core.shared.exception.AccessDeniedException;
import com.zhenbanban.core.shared.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 拦截器 : AdminPermitInterceptor
 *
 * @author zhangxihai 2025/8/4
 */
@Component
@RequiredArgsConstructor
public class AdminPermitInterceptor implements HandlerInterceptor {
    private final IAuth<Admin> auth;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        AdminPermit annotation = handlerMethod.getMethodAnnotation(AdminPermit.class);
        if (annotation == null) {
            return true; // 如果没有注解，直接放行
        }

        Admin admin = auth.user();

        if (admin == null) {
            throw new UnauthorizedException();
        }

        // 检查角色
        List<String> permitRoles = Arrays.asList(annotation.roles());
        permitRoles = permitRoles.isEmpty() ? List.of("administrator") : permitRoles;

        Set<String> roleNames = admin.getRoleNames() == null ? new HashSet<>() : new HashSet<>(admin.getRoleNames());
        for (String role : permitRoles) {
            if (roleNames.contains(role)) {
                return true; // 有角色则放行
            }
        }

        // 检查权限
        String[] permitPermissions = annotation.permissions();
        if (permitPermissions != null && permitPermissions.length > 0) {
            Set<String> permissions = admin.getPermissionNames() == null ? new HashSet<>() : new HashSet<>(admin.getPermissionNames());
            for (String permission : permitPermissions) {
                if (permissions.contains(permission)) {
                    return true; // 有权限则放行
                }
            }
        }

        String message = annotation.message();
        message = message == null || message.isEmpty() ? "您未被授权进行此操作" : message;

        throw new AccessDeniedException(message);
    }
}
