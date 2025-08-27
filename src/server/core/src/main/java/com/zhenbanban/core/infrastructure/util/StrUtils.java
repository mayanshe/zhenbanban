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
 * 工具类：字符串处理
 *
 * @author zhangxihai 2025/08/01
 */
public final class StrUtils {
    private StrUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 将输入字符串转换为MD5哈希值
     *
     * @param input 输入字符串
     * @return MD5哈希值的十六进制表示
     */
    public static String convertToMd5(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 验证是否为有效的中国手机号码
     *
     * @param phoneNumber 手机号码字符串
     * @return 如果是有效的中国手机号码则返回true，否则返回false
     */
    public static boolean isValidChinesePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        String regex = "^1[3-9]\\d{9}$";
        return phoneNumber.matches(regex);
    }

    /**
     * 验证是否为有效的电子邮件地址
     *
     * @param email 电子邮件地址字符串
     * @return 如果是有效的电子邮件地址则返回true，否则返回false
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    /**
     * 脱敏电子邮件地址
     *
     * @param email 电子邮件地址字符串
     * @return 脱敏后的电子邮件地址
     */
    public static String desensitizeEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email; // 如果格式不正确，返回原始邮箱
        }
        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.length() <= 2) {
            return email; // 如果本地部分长度小于等于2，返回原始邮箱
        }

        StringBuilder desensitizedLocalPart = new StringBuilder();
        desensitizedLocalPart.append(localPart.charAt(0)); // 保留第一个字符
        for (int i = 1; i < localPart.length() - 1; i++) {
            desensitizedLocalPart.append('*'); // 中间部分用星号替换
        }
        desensitizedLocalPart.append(localPart.charAt(localPart.length() - 1)); // 保留最后一个字符

        return desensitizedLocalPart + "@" + domainPart;
    }

    /**
     * 脱敏中国手机号码
     *
     * @param phoneNumber 手机号码字符串
     * @return 脱敏后的手机号码
     */
    public static String desensitizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }
        if (!isValidChinesePhoneNumber(phoneNumber)) {
            return phoneNumber; // 如果格式不正确，返回原始手机号
        }
        return phoneNumber.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*");
    }

}
