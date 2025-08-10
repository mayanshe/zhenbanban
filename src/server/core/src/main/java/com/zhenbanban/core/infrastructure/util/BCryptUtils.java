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

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utils
 *
 * @author zhangxihai 2025/8/3
 */
public final class BCryptUtils {
    private BCryptUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 使用BCrypt算法对输入字符串进行加密
     *
     * @param input 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String hash(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt());
    }

    /**
     * 验证明文密码与加密后的密码是否匹配
     *
     * @param plain 明文密码
     * @param hashed 加密后的密码
     * @return 如果匹配返回true，否则返回false
     */
    public static boolean verify(String plain, String hashed) {
        try {
            return BCrypt.checkpw(plain, hashed);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
