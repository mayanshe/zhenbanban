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
 * Po: 中药饮片
 *
 * @author zhangxihai 2025/08/31
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChineseMedicinePiecePo {
    private Long id;

    private String pieceCode;

    private String pieceName;

    private String pieceNamePinyin;

    private String pieceNamePinyinAbbr;

    private String pieceAlias;

    private String pieceAliasPinyin;

    private String pieceAliasPinyinAbbr;

    private String nature;

    private String meridian;

    private String indications;

    private String dosage;

    @Builder.Default
    private long createdAt = 0L;

    @Builder.Default
    private long updatedAt = 0L;

    @Builder.Default
    private long deletedAt = 0L;
}
