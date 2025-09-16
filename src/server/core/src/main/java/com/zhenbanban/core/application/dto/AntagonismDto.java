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

import lombok.Data;

/**
 * Dto: 十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Data
public class AntagonismDto {
    private String id;

    private Short kind;                 // 种类(18-十八反,19-十九畏)

    private String kindName;            // 种类名称

    private String pieceId;               // 药材编号

    private String pieceCode;              // 药材编码

    private String pieceName;              // 药材名称

    private String pieceAlias;            // 药材别名

    private String conflictPieceId;       // 相克编号

    private String conflictPieceCode;     // 相克编码

    private String conflictPieceName;     // 相克名称

    private String conflictPieceAlias;    // 相克别名

    public String getKindName() {
        if (this.kind != null) {
            if (this.kind == 18) {
                return "十八反";
            } else if (this.kind == 19) {
                return "十九畏";
            }
        }
        return "";
    }

}
