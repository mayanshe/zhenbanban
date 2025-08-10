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
package com.zhenbanban.core.infrastructure.support;

import com.zhenbanban.core.domain.common.AbsDomainEvent;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.infrastructure.persistence.mapper.EventPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.EventPo;
import com.zhenbanban.core.shared.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 命令载体: 角色增删改
 *
 * @author zhangxihai 2025/08/01
 */
@Component
@RequiredArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventPoMapper eventMapper;

    @Override
    @Transactional
    public void publish(AbsDomainEvent event) {
        applicationEventPublisher.publishEvent(event);
        confirmOrRecord(event);
    }

    @Override
    @Transactional
    public void publish(List<? extends AbsDomainEvent> events) {
        for (AbsDomainEvent event : events) {
            publish(event);
        }
    }

    /**
     * 确认事件发布成功或记录事件
     *
     * @param event 领域事件
     */
    private void confirmOrRecord(AbsDomainEvent event) {
        EventPo po = eventMapper.findByEventId(event.getEventId());

        // 更新
        if (po != null) {
            po.setState((short) 1);
            eventMapper.update(po);
            return;
        }

        // 新建
        Long refId = event.getRefId();
        if (refId == null || refId <= 0) {
            refId = 0L;
        }

        po = EventPo.builder()
                .refId(refId)
                .eventType(event.getEventType())
                .eventId(event.getEventId())
                .eventData(event.toString())
                .occurredAt(event.getOccurredAt())
                .createdBy(event.getCreatedBy())
                .state((short) 1)
                .version(0L)
                .build();
        if (eventMapper.insert(po) <= 0) {
            throw new InternalServerException();
        }
    }

}
