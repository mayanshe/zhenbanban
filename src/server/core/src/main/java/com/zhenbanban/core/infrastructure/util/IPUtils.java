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

/**
 * Util : IP工具了
 *
 * @author zhangxihai 2025/8/7
 */
public final class IPUtils {
    private IPUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static long ipv4ToLong(byte[] bytes) {
        long result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }

    public static long[] ipv6ToLongs(byte[] bytes) {
        long[] result = new long[2];
        for (int i = 0; i < 8; i++) {
            result[0] = (result[0] << 8) | (bytes[i] & 0xFF);
        }
        for (int i = 8; i < 16; i++) {
            result[1] = (result[1] << 8) | (bytes[i] & 0xFF);
        }
        return result;
    }

}
