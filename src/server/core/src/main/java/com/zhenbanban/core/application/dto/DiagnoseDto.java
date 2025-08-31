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
package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.infrastructure.util.DateUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;

/**
 * Dto : 疾病诊断
 *
 * @author zhangxihai 2025/8/27
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiagnoseDto {
    private String id;                             // 主键ID

    private Integer icdType;                     // 诊断类型 1-ICD-10 2-ICD-9 3-ICD-11

    private String icdCode;                      // 诊断编码

    private String icdName;                      // 诊断名

    private String icdNamePinyin;                // 诊断名拼音

    private String icdNamePinyinAbbr;            //  诊断名拼音首字母

    private String icdOptionalName;              // 可选用名

    private String icdOptionalNamePinyin;        // 可选用名拼音

    private String icdOptionalNamePinyinAbbr;    // 可选用名拼音首字母

    private String icdAliasName;                 // 别名

    private String icdAliasNamePinyin;           // 别名拼音

    private String icdAliasNamePinyinAbbr;       // 别名拼音首字母

    private String description;                  // 诊断描述

    private String chapterCode;                  // 章节编码

    private String chapterName;                  // 章节名称

    private String blockCode;                    // 疾病组编码

    private String blockName;                    // 疾病组名称

    private String createdAt;                      // 创建时间

    private String updatedAt;                      // 最后修改时间

    private String deletedAt;                      // 删除时间 0-未删除，非0-删除时间

    public String getCreatedAt() {
        return createdAt == null || createdAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt == null || updatedAt.isBlank() ? "" : DateUtils.timestampToFormattedDate(updatedAt);
    }

    public String getDeletedAt() {
        return deletedAt == null || deletedAt.isBlank() || "0".equals(deletedAt) ? "" : DateUtils.timestampToFormattedDate(deletedAt);
    }

}
