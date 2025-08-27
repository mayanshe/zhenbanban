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

import com.zhenbanban.core.application.command.MediaAmdCmdHandler;
import com.zhenbanban.core.application.dto.MediaCreateCommand;
import com.zhenbanban.core.application.dto.MediaDto;
import com.zhenbanban.core.application.dto.MediaModifyCommand;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.domain.systemcontext.entity.Media;
import com.zhenbanban.core.domain.systemcontext.respository.MediaRepository;
import com.zhenbanban.core.infrastructure.external.cos.CosProvider;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Command Achieve : 媒体资源
 *
 * @author zhangxihai 2025/8/26
 */
@Service
public class MediaAmdCmdHandlerImpl implements MediaAmdCmdHandler {
    private final MediaRepository mediaRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    private final CosProvider cosProvider;

    @Autowired
    public MediaAmdCmdHandlerImpl(
            @Lazy MediaRepository mediaRepository,
            @Lazy DomainEventPublisher domainEventPublisher,
            @Lazy IdGenerator idGenerator,
            @Lazy CosProvider cosProvider
    ) {
        this.mediaRepository = mediaRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.idGenerator = idGenerator;
        this.cosProvider = cosProvider;
    }

    @Override
    public MediaDto handleAdd(MediaCreateCommand cmd) {
        Long id = idGenerator.nextId();

        Media media = (new ModelMapper()).map(cmd, Media.class);
        media.setId(id);
        media.add();

        mediaRepository.save(media, true);
        domainEventPublisher.publish(media.getEvents());

        return (new ModelMapper()).map(media, MediaDto.class);
    }

    @Override
    public void handleModify(MediaModifyCommand cmd) {
        Media media = mediaRepository.load(cmd.getId());
        (new ModelMapper()).map(cmd, media);
        media.modify();

        mediaRepository.save(media, false);
        domainEventPublisher.publish(media.getEvents());
    }

    @Override
    public void handleDestroy(Long key) {
        Media media = mediaRepository.load(key);
        media.destroy();

        cosProvider.delete(media.getFilePath());

        mediaRepository.save(media, false);
        domainEventPublisher.publish(media.getEvents());
    }

}
