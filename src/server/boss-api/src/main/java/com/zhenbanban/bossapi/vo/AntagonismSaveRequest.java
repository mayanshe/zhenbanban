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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * VO: 十八反十九畏保存请求
 *
 * @author zhangxihai 2025/09/03
 */
@Data
public class AntagonismSaveRequest {

    @NotEmpty(message = "饮片编码不能为空")
    private String pieceCode;

    @NotEmpty(message = "饮片名称不能为空")
    private String pieceName;

    @NotEmpty(message = "配伍编码不能为空")
    private String antagonismPieceCodes;

    @NotEmpty(message = "配伍名称不能为空")
    private String antagonismPieceNames;

    @NotNull(message = "类型不能为空")
    private Integer type;

    private String remark;
}
