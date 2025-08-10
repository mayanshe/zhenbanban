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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Util : 日期和时间工具类
 *
 * @author zhangxihai 2025/8/4
 */
public final class DateAndTimeUtils {
    private DateAndTimeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 获取当前时间到当天结束的秒数
     *
     * @return 当前时间到当天结束的秒数
     */
    public static long getSecondsToEndOfDay() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime endOfDay = now.toLocalDate()
                .atTime(LocalTime.MAX)
                .minusNanos(1);

        return ChronoUnit.SECONDS.between(now, endOfDay);
    }

}
