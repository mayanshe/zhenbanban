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
    private Long id;                              // 主键ID

    private Integer icdType;                      // ICD 版本类型

    private String icdCode;                       // 诊断代码

    private String icdName;                       // 诊断名称

    private String icdNamePinyin;                 // 诊断拼音

    private String icdNamePinyinAbbr;             // 诊断拼音首字母

    private String icdOptionalName;              // 可选用名

    private String icdOptionalNamePinyin;        // 可选用名拼音

    private String icdOptionalNamePinyinAbbr;    // 可选用名拼音首字母

    private String icdAliasName;                 // 别名

    private String icdAliasNamePinyin;           // 别名拼音

    private String icdAliasNamePinyinAbbr;       // 别名拼音首字母

    private String description;                  // 诊断描述

    private String chapterCode;                  // 章节代码

    private String chapterName;                  // 章节名称

    private String blockCode;                    // 章代码

    private String blockName;                    // 章名称

    @Builder.Default
    private long createdAt = 0L;                 // 创建时间

    @Builder.Default
    private long updatedAt = 0L;                 // 最后修改时间

    @Builder.Default
    private long deletedAt = 0L;                 // 删除时间

}
