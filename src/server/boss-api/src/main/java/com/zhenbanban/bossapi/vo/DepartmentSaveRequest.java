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

import com.zhenbanban.core.infrastructure.support.annotation.InList;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VO: 科室保存请求
 *
 * @author zhangxihai 2025/09/16
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSaveRequest {

    @Builder.Default
    private Long parentId = 0L;

    @NotEmpty(message = "科室类型不能为空")
    @InList(value = {"clinical", "technology", "emergency", "logistics"}, message = "科室类型选择错误")
    private String departmentType;

    @NotEmpty(message = "科室名称不能为空")
    @Size(max = 75, message = "科室名称不能超过75个字符")
    private String departmentName;

    @Builder.Default
    @Size(max = 512, message = "科室简介不能超过512个字符")
    private String summary = "";

    @Builder.Default
    private String description = "";

}
