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

import com.zhenbanban.core.domain.dictionarycontext.entity.Department;
import com.zhenbanban.core.domain.dictionarycontext.repository.DepartmentRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.DepartmentConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.DepartmentPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.DepartmentPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 领域仓储实现: 互联网医院科室
 *
 * @author zhangxihai 2025/09/16
 */
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentPoMapper departmentMapper;

    public DepartmentRepositoryImpl(@Lazy DepartmentPoMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Department load(Long id) {
        DepartmentPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的科室信息");
        }
        return DepartmentConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Department aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (departmentMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除科室失败");
            }
            return aggregate.getId();
        }

        verify(aggregate);

        if (isNew) {
            DepartmentPo po = DepartmentConverter.INSTANCE.toPo(aggregate);
            if (departmentMapper.insert(po) <= 0) {
                throw new InternalServerException("添加科室失败");
            }
            return po.getId();
        }

        DepartmentPo po = getPo(aggregate.getId());
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的科室信息，无法更新");
        }
        DepartmentPo updatedPo = DepartmentConverter.INSTANCE.updatePo(aggregate, po);
        if (departmentMapper.update(updatedPo) <= 0) {
            throw new InternalServerException("更新科室失败");
        }

        return aggregate.getId();
    }

    private void verify(Department aggregate) {
        verifyDepartmentExists(aggregate);
    }

    private void verifyDepartmentExists(Department aggregate) {
        Long id = departmentMapper.findIdByName(aggregate.getDepartmentName());
        if (id != null && !id.equals(aggregate.getId())) {
            throw new RequestConflictException(String.format("科室名称 '%s' 已存在", aggregate.getDepartmentName()));
        }
    }

    private DepartmentPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return departmentMapper.findById(id);
    }
}
