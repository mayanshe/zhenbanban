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

import com.zhenbanban.core.domain.dictionarycontext.entity.Diagnose;
import com.zhenbanban.core.domain.dictionarycontext.repository.DiagnoseRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.DiagnoseConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.DiagnosePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.DiagnosePo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import com.zhenbanban.core.shared.exception.ServiceUnavailableException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现 : 疾病诊断
 *
 * @author zhangxihai 2025/8/03
 */
@Repository
public class DiagnoseRepositoryImpl implements DiagnoseRepository {
    private final DiagnosePoMapper diagnoseMapper;

    public DiagnoseRepositoryImpl(
            @Lazy DiagnosePoMapper diagnoseMapper
    ) {
        this.diagnoseMapper = diagnoseMapper;
    }

    @Override
    public Diagnose load(Long id) {
        DiagnosePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的疾病诊断信息");
        }

        return DiagnoseConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Diagnose aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (diagnoseMapper.delete(aggregate.getId()) <= 0) {
                throw new ServiceUnavailableException("删除疾病诊断失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        DiagnosePo po = DiagnoseConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        if (isNew) {
            if (diagnoseMapper.insert(po) <= 0) {
                throw new ServiceUnavailableException("添加疾病诊断失败");
            }
            return po.getId();
        }

        if (diagnoseMapper.update(po) <= 0) {
            throw new InternalServerException("更新疾病诊断失败");
        }

        return po.getId();
    }

    private void verify(Diagnose aggregate) {
        verifyDiagnoseExists(aggregate);
    }

    private void verifyDiagnoseExists(Diagnose aggregate) {
        Long id = diagnoseMapper.findIdByCodeAndName(aggregate.getIcdCode(), aggregate.getIcdName());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("疾病诊断 '%s' 已存在", aggregate.getIcdName()));
        }
    }

    private DiagnosePo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return diagnoseMapper.findById(id);
    }
}
