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
package com.zhenbanban.core.infrastructure.persistence.repository;

import com.zhenbanban.core.domain.dictionarycontext.entity.Antagonism;
import com.zhenbanban.core.domain.dictionarycontext.repository.AntagonismRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.AntagonismConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.AntagonismPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.AntagonismPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现 : 十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Repository
public class AntagonismRepositoryImpl implements AntagonismRepository {
    private final AntagonismPoMapper antagonismMapper;

    public AntagonismRepositoryImpl(@Lazy AntagonismPoMapper antagonismMapper) {
        this.antagonismMapper = antagonismMapper;
    }

    @Override
    public Antagonism load(Long id) {
        AntagonismPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的十八反十九畏信息");
        }
        return AntagonismConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Antagonism aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (antagonismMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除十八反十九畏信息失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        AntagonismPo po = AntagonismConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        long timestamp = System.currentTimeMillis();

        if (isNew) {
            po.setGmtCreated(timestamp);
            po.setGmtModified(timestamp);
            if (antagonismMapper.insert(po) <= 0) {
                throw new InternalServerException("添加十八反十九畏信息失败");
            }
            return po.getId();
        }

        po.setGmtModified(timestamp);
        if (antagonismMapper.update(po) <= 0) {
            throw new InternalServerException("更新十八反十九畏信息失败");
        }

        return po.getId();
    }

    private void verify(Antagonism aggregate) {
        Long id = antagonismMapper.findIdByCode(aggregate.getPieceCode());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("饮片 '%s' 的十八反十九畏信息已存在", aggregate.getPieceName()));
        }
    }

    private AntagonismPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return antagonismMapper.findById(id);
    }
}
