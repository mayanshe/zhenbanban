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
package com.zhenbanban.core.domain.dictionarycontext.entity;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.dictionarycontext.event.AntagonismAddedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.AntagonismDestroyedEvent;
import com.zhenbanban.core.domain.dictionarycontext.event.AntagonismModifiedEvent;
import lombok.*;

/**
 * 聚合根：十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Antagonism extends AbsAggregate {
    private Long id;

    private Short Kind;                      // 种类(18-十八反,19-十九畏)

    private Long pieceId;                    // 饮片ID

    private String pieceCode;                // 饮片编码

    private String pieceName;                // 饮片名称

    private String pieceAlias;               // 饮片别名

    private Long conflictPieceId;            // 冲突饮片ID

    private String conflictPieceCode;        // 冲突饮片编码

    private String conflictPieceName;        // 冲突饮片名称

    private String conflictPieceAlias;       // 冲突饮片别名

    @Builder.Default
    private boolean deleted = false;         // 是否已删除

    /**
     * 添加
     */
    public void add() {
        this.setDeleted(false);

        AntagonismAddedEvent event = AntagonismAddedEvent.builder()
                .refId(this.getId())
                .antagonismId(this.getId())
                .pieceId(this.getPieceId())
                .pieceCode(this.getPieceCode())
                .pieceName(this.getPieceName())
                .conflictPieceId(this.getConflictPieceId())
                .conflictPieceCode(this.getConflictPieceCode())
                .conflictPieceName(this.getConflictPieceName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改
     */
    public void modify() {
        this.setDeleted(false);

        AntagonismModifiedEvent event = AntagonismModifiedEvent.builder()
                .refId(this.getId())
                .antagonismId(this.getId())
                .pieceId(this.getPieceId())
                .pieceCode(this.getPieceCode())
                .pieceName(this.getPieceName())
                .conflictPieceId(this.getConflictPieceId())
                .conflictPieceCode(this.getConflictPieceCode())
                .conflictPieceName(this.getConflictPieceName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁
     */
    public void destroy() {
        this.setDeleted(true);

        AntagonismDestroyedEvent event = AntagonismDestroyedEvent.builder()
                .refId(this.getId())
                .antagonismId(this.getId())
                .pieceId(this.getPieceId())
                .pieceCode(this.getPieceCode())
                .pieceName(this.getPieceName())
                .conflictPieceId(this.getConflictPieceId())
                .conflictPieceCode(this.getConflictPieceCode())
                .conflictPieceName(this.getConflictPieceName())
                .build();

        this.addEvent(event);
    }

}
