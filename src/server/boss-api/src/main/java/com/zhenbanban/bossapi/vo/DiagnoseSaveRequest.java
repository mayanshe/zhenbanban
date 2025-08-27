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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiagnoseSaveRequest {
    @NotBlank(message = "ICD类型不能为空")
    private Integer icdType;
    @NotBlank(message = "ICD编码不能为空")
    private String icdCode;
    @NotBlank(message = "ICD名称不能为空")
    private String icdName;
    private String icdNamePinyin;
    private String icdNamePinyinAbbr;
    private String icdOptionalName;
    private String icdOptionalNamePinyin;
    private String icdOptionalNamePinyinAbbr;
    private String icdAliasName;
    private String icdAliasNamePinyin;
    private String icdAliasNamePinyinAbbr;
    private String description;
    private String chapterCode;
    private String chapterName;
    private String blockCode;
    private String blockName;
}
