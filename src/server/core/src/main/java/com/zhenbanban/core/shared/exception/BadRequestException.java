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
package com.zhenbanban.core.shared.exception;

/**
 * 自定义异常: 错误请求异常
 *
 * @author zhangxihai 2025/08/01
 */
public class BadRequestException extends AbsBaseException {
    public BadRequestException() {
        super("Bad Request", "请求错误, 请重试");
    }

    public BadRequestException(String message) {
        super("Bad Request", message);
    }

    public BadRequestException(String message, Throwable cause) {
        super("Bad Request", message, cause);
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }

}
