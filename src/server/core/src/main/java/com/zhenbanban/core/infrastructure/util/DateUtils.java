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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类: 日期时间处理
 *
 * @author zhangxihai 2025/08/01
 */
public final class DateUtils {
    public DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 将原始时间戳（格式为yyyyMMddHHmmss）向下舍入到5秒的间隔，并返回格式化后的字符串（yyyyMMddHHmm）
     *
     * @param originalDateTime 原始时间戳
     * @return 格式化后的时间字符串
     */
    public static String roundSecondsToFive(String originalDateTime) {
        // 解析原始时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, formatter);

        // 获取秒数并向下舍入到5秒间隔
        int second = dateTime.getSecond();
        int roundedSecond = (second / 5) * 5;  // 整数除法自动向下取整

        // 创建新的时间对象并格式化
        LocalDateTime roundedDateTime = dateTime.withSecond(roundedSecond);
        return roundedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 将时间戳转换为格式化的日期字符串
     *
     * @param timestamp 时间戳（秒）
     * @return 格式化后的日期字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String timestampToFormattedDate(long timestamp) {
        timestamp = timestamp <= 99_999_999_999L ? timestamp : timestamp / 1000;

        if (timestamp <= 0) {
            return "";
        }

        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将时间戳字符串转换为格式化的日期字符串
     *
     * @param timestamp 时间戳字符串（秒）
     * @return 格式化后的日期字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String timestampToFormattedDate(String timestamp) {
        try {
            long ts = Long.parseLong(timestamp);
            return timestampToFormattedDate(ts);
        } catch (NumberFormatException e) {
            return "";
        }
    }
}
