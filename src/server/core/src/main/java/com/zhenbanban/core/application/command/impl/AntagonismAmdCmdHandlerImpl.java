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
package com.zhenbanban.core.application.command.impl;

import com.zhenbanban.core.application.command.AntagonismAmdCmdHandler;
import com.zhenbanban.core.application.dto.AntagonismAmdCommand;
import com.zhenbanban.core.domain.dictionarycontext.entity.Antagonism;
import com.zhenbanban.core.domain.dictionarycontext.entity.ChineseMedicinePiece;
import com.zhenbanban.core.domain.dictionarycontext.repository.AntagonismRepository;
import com.zhenbanban.core.domain.common.repository.DomainEventPublisher;
import com.zhenbanban.core.domain.dictionarycontext.repository.ChineseMedicinePieceRepository;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现 : 十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Service
@RequiredArgsConstructor
public class AntagonismAmdCmdHandlerImpl implements AntagonismAmdCmdHandler {
    private final AntagonismRepository antagonismRepository;

    private final ChineseMedicinePieceRepository pieceRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(AntagonismAmdCommand command) {
        Long id = idGenerator.nextId();

        ChineseMedicinePiece piece = pieceRepository.load(command.getPieceId());
        ChineseMedicinePiece conflictPiece = pieceRepository.load(command.getConflictPieceId());

        ModelMapper modelMapper = new ModelMapper();
        Antagonism antagonism = modelMapper.map(command, Antagonism.class);
        antagonism.setId(id);
        antagonism.setPieceCode(piece.getPieceCode());
        antagonism.setPieceName(piece.getPieceName());
        antagonism.setPieceAlias(piece.getPieceAlias());
        antagonism.setConflictPieceCode(conflictPiece.getPieceCode());
        antagonism.setConflictPieceName(conflictPiece.getPieceName());
        antagonism.setConflictPieceAlias(conflictPiece.getPieceAlias());
        antagonism.add();

        antagonismRepository.save(antagonism, true);
        domainEventPublisher.publish(antagonism.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(AntagonismAmdCommand command) {
        Antagonism antagonism = antagonismRepository.load(command.getId());

        if (!command.getPieceId().equals(antagonism.getPieceId())) {
            ChineseMedicinePiece piece = pieceRepository.load(command.getPieceId());
            antagonism.setPieceId(piece.getId());
            antagonism.setPieceCode(piece.getPieceCode());
            antagonism.setPieceName(piece.getPieceName());
        }

        if (!command.getConflictPieceId().equals(antagonism.getConflictPieceId())) {
            ChineseMedicinePiece conflictPiece = pieceRepository.load(command.getConflictPieceId());
            antagonism.setConflictPieceId(conflictPiece.getId());
            antagonism.setConflictPieceCode(conflictPiece.getPieceCode());
            antagonism.setConflictPieceName(conflictPiece.getPieceName());
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, antagonism);
        antagonism.modify();

        antagonismRepository.save(antagonism, false);
        domainEventPublisher.publish(antagonism.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Antagonism antagonism = antagonismRepository.load(id);
        antagonism.destroy();

        antagonismRepository.save(antagonism, false);
        domainEventPublisher.publish(antagonism.getEvents());
    }

}
