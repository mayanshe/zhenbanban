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
package com.zhenbanban.bossapi.vo;

import com.zhenbanban.core.infrastructure.support.annotation.NotBlankPattern;
import com.zhenbanban.core.infrastructure.support.annotation.NotBlankSize;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vo : 更新账户信息请求
 *
 * @author zhangxihai 2025/8/4
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAccountRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 24, message = "用户名长度必须在4到24个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;              // 用户名，唯一

    @NotBlankSize(min = 8, max = 64, message = "密码长度必须在8到64个字符之间")
    @NotBlankPattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符")
    private String password;              // 密码，至少8个字符

    @Size(max = 75, message = "姓氏不能超过75个字符")
    @Builder.Default
    private String surname = "";          // 姓氏

    @Size(max = 75, message = "名字不能超过75个字符")
    @Builder.Default
    private String givenName = "";        // 名字

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @Builder.Default
    private short gender = 0;             // 性别，0-未知，1-男，2-女

    @Builder.Default
    @NotBlankPattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "邮箱地址格式不正确")
    private String email = "";            // 邮箱地址，唯一

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "手机号码格式不正确")
    private String phone;                 // 手机号码，唯一

    @Builder.Default
    private String avatar = "";           // 头像URL，默认为空字符串

}
