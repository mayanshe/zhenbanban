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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类：打印相关的工具方法
 *
 * @author zhangxihai 2025/08/01
 */
public final class PrintUtils {
    private PrintUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void info(String message, Object... args) {
        if (args != null && args.length > 0) {
            ObjectMapper objectMapper = new ObjectMapper();

            for (Object arg : args) {
                try {
                    String json = objectMapper.writeValueAsString(arg);
                    message = message.replaceFirst("\\{\\}", json);
                } catch (Exception ignore) {}
            }
        }

        System.out.printf("\u001B[34m%s\u001B[0m %n",  message);
    }

    public static void error(String message, Object... args) {
        if (args != null && args.length > 0) {
            ObjectMapper objectMapper = new ObjectMapper();

            for (Object arg : args) {
                try {
                    String json = objectMapper.writeValueAsString(arg);
                    message = message.replaceFirst("\\{\\}", json);
                } catch (Exception ignore) {}
            }
        }

        System.out.printf("\u001B[31m%s\u001B[0m %n",  message);
    }

    /**
     * 打印到控制台命令行
     *
     * @param args
     */
    public static void toConsole(Object... args) {
        if (args == null || args.length == 0) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (Object arg : args) {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                String json = objectMapper.writeValueAsString(arg);
                System.out.println("------------------------------------------------------------------------------------");
                if (json.startsWith("\"SQL")) {
                    System.out.printf("\u001B[93m[%s]\u001B[0m \u001B[32m %s \u001B[0m %n", formattedDateTime, json);
                } else {
                    System.out.printf("\u001B[93m[%s]\u001B[0m \u001B[36m %s \u001B[0m %n", formattedDateTime, json);
                }
                System.out.println("                       ^");
            } catch (Exception e) {
                System.err.println("打印错误: " + e.getMessage());
            }
        }
    }

    /**
     * 打印到指定文件
     *
     * @param file 文件路径
     * @param args 可变参数，任意数量的对象
     */
    public static void toFile(String file, Object... args) {
        if (file == null || file.isEmpty() || args == null || args.length == 0) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        for (Object arg : args) {
            try {

                String json = objectMapper.writeValueAsString(arg);
                sb.append(String.format("[%s] %s", formattedDateTime, json)).append(System.lineSeparator()).append(System.lineSeparator());
            } catch (Exception e) {
                System.err.println("Error converting object to JSON: " + e.getMessage());
            }
        }

        try {
            Path path = Paths.get(file);
            Files.write(path, sb.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * 发送到指定邮箱地址
     *
     * @param email 邮箱地址
     * @param args  可变参数，任意数量的对象
     */
    public static void toEmail(String email, Object... args) {
        if (email == null || email.isEmpty() || args == null || args.length == 0) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            try {
                String json = objectMapper.writeValueAsString(arg);
                sb.append(json).append(System.lineSeparator());
            } catch (Exception e) {
                System.err.println("Error converting object to JSON: " + e.getMessage());
            }
        }

        // todo: 实现发送邮件的逻辑
    }

}
