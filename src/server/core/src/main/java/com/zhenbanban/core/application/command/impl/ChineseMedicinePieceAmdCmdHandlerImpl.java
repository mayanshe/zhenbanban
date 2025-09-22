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

import com.zhenbanban.core.application.command.ChineseMedicinePieceAmdCmdHandler;
import com.zhenbanban.core.application.dto.ChineseMedicinePieceAmdCommand;
import com.zhenbanban.core.domain.dictionarycontext.entity.ChineseMedicinePiece;
import com.zhenbanban.core.domain.dictionarycontext.repository.ChineseMedicinePieceRepository;
import com.zhenbanban.core.domain.common.repository.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 命令实现 : 中药饮片
 *
 * @author zhangxihai 2025/08/31
 */
@Service
@RequiredArgsConstructor
public class ChineseMedicinePieceAmdCmdHandlerImpl implements ChineseMedicinePieceAmdCmdHandler {
    private final ChineseMedicinePieceRepository chineseMedicinePieceRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(ChineseMedicinePieceAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper modelMapper = new ModelMapper();
        ChineseMedicinePiece piece = modelMapper.map(command, ChineseMedicinePiece.class);
        piece.setId(id);
        piece.add();

        chineseMedicinePieceRepository.save(piece, true);
        domainEventPublisher.publish(piece.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(ChineseMedicinePieceAmdCommand command) {
        ChineseMedicinePiece piece = chineseMedicinePieceRepository.load(command.getId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(command, piece);
        piece.modify();

        chineseMedicinePieceRepository.save(piece, false);
        domainEventPublisher.publish(piece.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        ChineseMedicinePiece piece = chineseMedicinePieceRepository.load(id);
        piece.destroy();

        chineseMedicinePieceRepository.save(piece, false);
        domainEventPublisher.publish(piece.getEvents());
    }

}
