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

import com.zhenbanban.core.domain.accountcontext.entity.Account;
import com.zhenbanban.core.infrastructure.util.JwtAuthUtils;
import com.zhenbanban.core.shared.contract.IAuth;
import com.zhenbanban.core.shared.exception.AccessDeniedException;
import com.zhenbanban.core.shared.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器 : AccountScopePermitInterceptor
 *
 * @author zhangxihai 2025/8/4
 */
@Component
public class AccountScopePermitInterceptor implements HandlerInterceptor {
    private final IAuth<Account> auth;

    public AccountScopePermitInterceptor(@Lazy IAuth<Account> auth) {
        this.auth = auth;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnauthorizedException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        AccountScopePermit annotation = handlerMethod.getMethodAnnotation(AccountScopePermit.class);
        if (annotation == null) {
            return true; // 如果没有注解，直接放行
        }

        // 获取当前用户
        Account account = auth.user();
        if (account == null) {
            throw new UnauthorizedException("您尚未登录或登录已过期，请重新登录");
        }

        // 检查账号范围
        String[] permitScopes = annotation.scopes();
        if (permitScopes.length == 0) {
            return true;
        }

        for (String scope : permitScopes) {
            if (account.getScopes() != null && account.getScopes().contains(scope)) {
                return true; // 如果账号范围中包含任意一个允许的范围，放行
            }
        }

        throw new AccessDeniedException("非法请求");
    }

}
