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

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Vo : 疾病诊断
 *
 * @author zhangxihai 2025/8/27
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseSaveRequest {
    @Min(value = 1, message = "诊断类型不能为空")
    private Integer icdType;

    @NotBlank(message = "诊断编码不能为空")
    @Size(max = 20, message = "ICD编码长度不能超过20个字符")
    private String icdCode;

    @NotBlank(message = "诊断名称不能为空")
    @Size(max = 255, message = "诊断名称长度不能超过200个字符")
    private String icdName;

    @Size(max = 255, message = "诊断可选用名长度不能超过200个字符")
    @Builder.Default
    private String icdOptionalName = "";

    @Size(max = 512, message = "诊断别名长度不能超过512个字符")
    @Builder.Default
    private String icdAliasName = "";

    @Builder.Default
    private String description = "";

    @Builder.Default
    @Size(max = 5, message = "章节编码长度不能超过5个字符")
    private String chapterCode = "";

    @Builder.Default
    @Size(max = 255, message = "章节名称长度不能超过255个字符")
    private String chapterName = "";

    @Builder.Default
    @Size(max = 10, message = "大类编码长度不能超过10个字符")
    private String blockCode = "";

    @Builder.Default
    @Size(max = 255, message = "大类名称长度不能超过255个字符")
    private String blockName = "";

}
