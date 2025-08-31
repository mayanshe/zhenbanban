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
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Vo : 中药饮片
 *
 * @author zhangxihai 2025/08/31
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChineseMedicinePieceSaveRequest {
    @NotBlank(message = "饮片编码不能为空")
    @Size(max = 10, message = "饮片编码长度不能超过10个字符")
    private String pieceCode;

    @NotBlank(message = "饮片名称不能为空")
    @Size(max = 255, message = "饮片名称长度不能超过255个字符")
    private String pieceName;

    @Size(max = 512, message = "饮片别名长度不能超过512个字符")
    @Builder.Default
    private String pieceAlias = "";

    @Size(max = 50, message = "性味长度不能超过50个字符")
    @Builder.Default
    private String nature = "";

    @Size(max = 50, message = "归经长度不能超过50个字符")
    @Builder.Default
    private String meridian = "";

    @Size(max = 255, message = "功能和主治长度不能超过255个字符")
    @Builder.Default
    private String indications = "";

    @Size(max = 125, message = "用法用量长度不能超过125个字符")
    @Builder.Default
    private String dosage = "";

}
