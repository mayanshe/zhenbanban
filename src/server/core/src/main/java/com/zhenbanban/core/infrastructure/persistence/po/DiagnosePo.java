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
package com.zhenbanban.core.infrastructure.persistence.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Po: 疾病诊断
 *
 * @author zhangxihai 2025/08/02
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosePo {
    private Long id;
    private Integer icdType;
    private String icdCode;
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
    @Builder.Default
    private long createdAt = 0L;
    @Builder.Default
    private long updatedAt = 0L;
    @Builder.Default
    private long deletedAt = 0L;
}
