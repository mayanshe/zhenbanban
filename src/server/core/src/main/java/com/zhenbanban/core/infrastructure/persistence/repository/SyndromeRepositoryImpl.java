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

import com.zhenbanban.core.domain.dictionarycontext.entity.Syndrome;
import com.zhenbanban.core.domain.dictionarycontext.repository.SyndromeRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.SyndromeConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.SyndromePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.SyndromePo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现 : 中医证候
 *
 * @author zhangxihai 2025/09/01
 */
@Repository
public class SyndromeRepositoryImpl implements SyndromeRepository {
    private final SyndromePoMapper syndromeMapper;

    public SyndromeRepositoryImpl(
            @Lazy SyndromePoMapper syndromeMapper
    ) {
        this.syndromeMapper = syndromeMapper;
    }

    @Override
    public Syndrome load(Long id) {
        SyndromePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的中医证候信息");
        }

        return SyndromeConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Syndrome aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (syndromeMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除中医证候失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        SyndromePo po = SyndromeConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        if (isNew) {
            if (syndromeMapper.insert(po) <= 0) {
                throw new InternalServerException("添加中医证候失败");
            }
            return po.getId();
        }

        if (syndromeMapper.update(po) <= 0) {
            throw new InternalServerException("更新中医证候失败");
        }

        return po.getId();
    }

    private void verify(Syndrome aggregate) {
        verifySyndromeExists(aggregate);
    }

    private void verifySyndromeExists(Syndrome aggregate) {
        Long id = syndromeMapper.findIdByCodeAndName(aggregate.getSyndromeCode(), aggregate.getSyndromeName());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("中医证候 '%s' 已存在", aggregate.getSyndromeName()));
        }
    }

    private SyndromePo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return syndromeMapper.findById(id);
    }
}
