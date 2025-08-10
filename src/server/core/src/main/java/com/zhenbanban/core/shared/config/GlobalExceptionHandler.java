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
package com.zhenbanban.core.shared.config;

import com.zhenbanban.core.infrastructure.util.EnvironmentChecker;
import com.zhenbanban.core.shared.exception.AbsBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.Map;

/**
 * 共享: 全局异常处理器配置类
 *
 * @author zhangxihai 2025/08/01
 */
@ControllerAdvice
@Profile("prod")
public class GlobalExceptionHandler {

    @Autowired
    private EnvironmentChecker environmentChecker;

    /**
     * 处理重复键异常
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "数据重复，请检查";
        if (!message.matches("uk_event_id")) {
            message = "请勿快速重复提交数据，数据已存在或冲突，请检查";
        } else {
            message = "数据重复，请检查";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", message);
        response.put("path", request.getDescription(false).replace("uri=", ""));
        response.put("timestamp", System.currentTimeMillis());

        /*if (environmentChecker.isDev()) {
            response.put("trace", ex.getStackTrace());
        }*/

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * 处理参数验证异常
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : "请求参数错误，请检查";
        if (!message.matches(".*[\u4e00-\u9fa5].*")) {
            message = "参数验证失败，请检查";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", message);
        response.put("details", ex.getBindingResult().getFieldErrors());
        response.put("path", request.getDescription(false).replace("uri=", ""));
        response.put("timestamp", System.currentTimeMillis());

        /*if (environmentChecker.isDev()) {
            response.put("trace", ex.getStackTrace());
        }*/

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 自定义的AbsBaseException异常处理
     *
     * @param ex      异常对象
     * @param request 请求对象
     * @return 响应实体
     */
    @ExceptionHandler(AbsBaseException.class)
    public ResponseEntity<Object> handleAbsBaseException(AbsBaseException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getHttpStatus());
        response.put("error", ex.getMessage());
        response.put("message", ex.getMessage());
        response.put("path", request.getDescription(false).replace("uri=", ""));
        response.put("timestamp", System.currentTimeMillis());

        /*if (environmentChecker.isDev()) {
            response.put("trace", ex.getStackTrace());
        }*/

        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    /**
     * 兜底的异常处理方法
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "网络异常，请稍后再试";
        if (!message.matches(".*[\u4e00-\u9fa5].*")) {
            message = "网络异常，请稍后再试";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", message);
        response.put("path", request.getDescription(false).replace("uri=", ""));
        response.put("timestamp", System.currentTimeMillis());

        /*if (environmentChecker.isDev()) {
            response.put("trace", ex.getStackTrace());
        }*/

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
