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
package com.zhenbanban.core.infrastructure.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类：HTTP处理
 *
 * @author zhangxihai 2025/08/01
 */
public final class HttpUtils {
    private HttpUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 获取当前请求的Header指定的字段的值
     *
     * @param headerName
     * @return
     */
    public static String getHeaderValue(String headerName) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader(headerName);
        }
        return null;
    }

    /**
     * 获取当前请求的参数指定的字段的值
     *
     * @param parameterName
     * @return
     */
    public static String getParameterValue(String parameterName) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getParameter(parameterName);
        }
        return null;
    }

    /**
     * 获取X-CSRF-TOKEN值，如果不存在则生成一个基于当前时间的默认值
     *
     * @return X-CSRF-TOKEN值
     */
    public static String getXCSRFToken() {
        String requestId = getHeaderValue("X-CSRF-TOKEN");
        if (requestId == null || requestId.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            requestId = DateUtils.roundSecondsToFive(now.format(formatter));
        }

        return requestId;
    }

    /**
     * 获取Authorization值，如果Header中不存在则尝试从参数中获取
     *
     * @return Authorization值
     */
    public static String getAuthorization() {
        String authorization = getHeaderValue("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            authorization = getParameterValue("token");
        }

        return authorization == null || authorization.isEmpty() ? "" : authorization;
    }

}
