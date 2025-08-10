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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Vo: 角色
 *
 * @author zhangxihai 2025/08/02
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleSaveRequest {
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 75, message = "角色名称不能超过75个字符")
    @Pattern(regexp = "^[a-zA-Z\\-]+$", message = "角色名称只能包含字母和-")
    private String roleName;

    @NotBlank(message = "角色显示名称不能为空")
    @Size(max = 75, message = "角色显示名称不能超过75个字符")
    private String displayName;

    @Size(max = 255, message = "角色描述不能超过255个字符")
    private String description;


}
