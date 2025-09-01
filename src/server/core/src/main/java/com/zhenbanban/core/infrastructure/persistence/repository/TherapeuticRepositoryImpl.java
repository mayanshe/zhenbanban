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

import com.zhenbanban.core.domain.dictionarycontext.entity.Therapeutic;
import com.zhenbanban.core.domain.dictionarycontext.repository.TherapeuticRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.TherapeuticConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.TherapeuticPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.TherapeuticPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现: 中医治法
 *
 * @author zhangxihai 2025/09/01
 */
@Repository
public class TherapeuticRepositoryImpl implements TherapeuticRepository {
    private final TherapeuticPoMapper therapeuticMapper;

    public TherapeuticRepositoryImpl(@Lazy TherapeuticPoMapper therapeuticMapper) {
        this.therapeuticMapper = therapeuticMapper;
    }

    @Override
    public Therapeutic load(Long id) {
        TherapeuticPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的中医治法信息");
        }
        return TherapeuticConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Therapeutic aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (therapeuticMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除中医治法失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        TherapeuticPo po = TherapeuticConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        if (isNew) {
            if (therapeuticMapper.insert(po) <= 0) {
                throw new InternalServerException("添加中医治法失败");
            }
            return po.getId();
        }

        if (therapeuticMapper.update(po) <= 0) {
            throw new InternalServerException("更新中医治法失败");
        }

        return po.getId();
    }

    private void verify(Therapeutic aggregate) {
        verifyTherapeuticExists(aggregate);
    }

    private void verifyTherapeuticExists(Therapeutic aggregate) {
        Long id = therapeuticMapper.findIdByCode(aggregate.getTherapeuticsCode());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("中医治法编码 '%s' 已存在", aggregate.getTherapeuticsCode()));
        }
    }

    private TherapeuticPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return therapeuticMapper.findById(id);
    }
}
