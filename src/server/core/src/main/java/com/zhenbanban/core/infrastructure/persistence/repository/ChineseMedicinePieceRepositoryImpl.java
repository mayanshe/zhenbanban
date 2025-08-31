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

import com.zhenbanban.core.domain.dictionarycontext.entity.ChineseMedicinePiece;
import com.zhenbanban.core.domain.dictionarycontext.repository.ChineseMedicinePieceRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.ChineseMedicinePieceConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.ChineseMedicinePiecePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.ChineseMedicinePiecePo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现 : 中药饮片
 *
 * @author zhangxihai 2025/08/31
 */
@Repository
public class ChineseMedicinePieceRepositoryImpl implements ChineseMedicinePieceRepository {
    private final ChineseMedicinePiecePoMapper chineseMedicinePieceMapper;

    public ChineseMedicinePieceRepositoryImpl(
            @Lazy ChineseMedicinePiecePoMapper chineseMedicinePieceMapper
    ) {
        this.chineseMedicinePieceMapper = chineseMedicinePieceMapper;
    }

    @Override
    public ChineseMedicinePiece load(Long id) {
        ChineseMedicinePiecePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的中药饮片信息");
        }

        return ChineseMedicinePieceConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(ChineseMedicinePiece aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (chineseMedicinePieceMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除中药饮片失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        ChineseMedicinePiecePo po = ChineseMedicinePieceConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        if (isNew) {
            if (chineseMedicinePieceMapper.insert(po) <= 0) {
                throw new InternalServerException("添加中药饮片失败");
            }
            return po.getId();
        }

        if (chineseMedicinePieceMapper.update(po) <= 0) {
            throw new InternalServerException("更新中药饮片失败");
        }

        return po.getId();
    }

    private void verify(ChineseMedicinePiece aggregate) {
        verifyChineseMedicinePieceExists(aggregate);
    }

    private void verifyChineseMedicinePieceExists(ChineseMedicinePiece aggregate) {
        Long id = chineseMedicinePieceMapper.findIdByCodeAndName(aggregate.getPieceCode(), aggregate.getPieceName());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("中药饮片 '%s' 已存在", aggregate.getPieceName()));
        }
    }

    private ChineseMedicinePiecePo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return chineseMedicinePieceMapper.findById(id);
    }
}
