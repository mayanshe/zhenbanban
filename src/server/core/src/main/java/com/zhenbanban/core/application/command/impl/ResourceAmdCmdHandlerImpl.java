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

import com.zhenbanban.core.application.command.ResourceAmdCmdHandler;
import com.zhenbanban.core.application.dto.ResourceAmdCommand;
import com.zhenbanban.core.domain.accountcontext.entity.Resource;
import com.zhenbanban.core.domain.accountcontext.repository.ResourceRepository;
import com.zhenbanban.core.domain.accountcontext.valueobj.ResourceType;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command Achieve : 资源
 *
 * @author zhangxihai 2025/7/11
 */
@Service
@RequiredArgsConstructor
public class ResourceAmdCmdHandlerImpl implements ResourceAmdCmdHandler {
    private final ResourceRepository resourceRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public Long handleAdd(ResourceAmdCommand command) {
        Long id = idGenerator.nextId();

        ModelMapper mapper = getMapper(command);
        Resource resource = mapper.map(command, Resource.class);
        resource.setId(id);
        resource.setResourceType(ResourceType.of(command.getResourceType()));
        resource.add();

        resourceRepository.save(resource, true);
        domainEventPublisher.publish(resource.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(ResourceAmdCommand command) {
        Resource resource = resourceRepository.load(command.getId());

        ModelMapper mapper = getMapper(command);
        mapper.map(command, resource);
        resource.setResourceType(ResourceType.of(command.getResourceType()));
        resource.modify();

        resourceRepository.save(resource, false);
        domainEventPublisher.publish(resource.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Resource resource = resourceRepository.load(id);

        resource.destroy();
        resourceRepository.save(resource, false);
        domainEventPublisher.publish(resource.getEvents());
    }

    private ModelMapper getMapper(ResourceAmdCommand command) {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.addMappings(new PropertyMap<ResourceAmdCommand, Resource>() {
            @Override
            protected void configure() {
                skip().setResourceType(null); // 跳过 resourceType 的映射
            }
        });

        return mapper;
    }

}
