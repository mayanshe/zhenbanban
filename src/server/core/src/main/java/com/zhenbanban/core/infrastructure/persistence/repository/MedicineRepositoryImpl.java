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

import com.zhenbanban.core.domain.dictionarycontext.entity.Medicine;
import com.zhenbanban.core.domain.dictionarycontext.repository.MedicineRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.MedicineConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.MedicinePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.MedicinePo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现 : 西药及中成药
 *
 * @author zhangxihai 2025/09/01
 */
@Repository
public class MedicineRepositoryImpl implements MedicineRepository {
    private final MedicinePoMapper medicineMapper;

    public MedicineRepositoryImpl(
            @Lazy MedicinePoMapper medicineMapper
    ) {
        this.medicineMapper = medicineMapper;
    }

    @Override
    public Medicine load(Long id) {
        MedicinePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的药品信息");
        }

        return MedicineConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Medicine aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (medicineMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除药品信息失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);
        MedicinePo po = MedicineConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        if (isNew) {
            if (medicineMapper.insert(po) <= 0) {
                throw new InternalServerException("添加药品信息失败");
            }
            return po.getId();
        }

        if (medicineMapper.update(po) <= 0) {
            throw new InternalServerException("更新药品信息失败");
        }

        return po.getId();
    }

    private void verify(Medicine aggregate) {
        verifyMedicineExists(aggregate);
    }

    private void verifyMedicineExists(Medicine aggregate) {
        Long id = medicineMapper.findIdByCodeAndName(aggregate.getMedicineCode(), aggregate.getMedicineName());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("药品 '%s' 已存在", aggregate.getMedicineName()));
        }
    }

    private MedicinePo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return medicineMapper.findById(id);
    }

}
