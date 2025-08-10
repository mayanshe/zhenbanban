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
package com.zhenbanban.core.infrastructure.support.annotation;

import com.zhenbanban.core.domain.common.AbsAggregate;
import com.zhenbanban.core.domain.common.AbsDomainEvent;
import com.zhenbanban.core.infrastructure.persistence.mapper.EventPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.EventPo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注解实现: 记录领域事件
 *
 * @author zhangxihai 2025/08/01
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class StoreDomainEventsAspect {
    private final EventPoMapper eventMapper;


    /**
     * 持久化领域事件
     *
     * @param joinPoint 切点
     * @param storeDomainEventsExecution 注解参数
     */
    @After("@annotation(storeDomainEventsExecution)")
    public void storeDomainEvents(JoinPoint joinPoint, StoreDomainEventsExecution storeDomainEventsExecution) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof AbsAggregate aggregate)) {
            return;
        }

        List<AbsDomainEvent> domainEvents = aggregate.getEvents();
        if (domainEvents == null || domainEvents.isEmpty()) {
            return;
        }

        Long aggregateId = aggregate.getId();

        for (AbsDomainEvent event : domainEvents) {
            Long refId = event.getRefId();

            if (aggregateId != null) {
                refId = aggregateId;      // 如果事件没有指定refId，则使用聚合根ID, 生成ID看起来实在太烦了， 能用自增就用自增
            }

            if (refId == null || refId <= 0) {
                refId = 0L;
            }

            EventPo eventPo = EventPo.builder()
                    .refId(refId)
                    .eventType(event.getEventType())
                    .eventId(event.getEventId())
                    .eventData(event.toString())
                    .occurredAt(event.getOccurredAt())
                    .createdBy(event.getCreatedBy())
                    .state((short) 0)
                    .build();
            eventMapper.insert(eventPo);
        }
    }

}
