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

import com.zhenbanban.core.domain.institutioncontext.entity.Hospital;
import com.zhenbanban.core.domain.institutioncontext.repository.HospitalRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.HospitalConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.HospitalPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.HospitalPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 聚合根Repository实现：医院
 *
 * @author zhangxihai 2025/08/11
 */
@Repository
public class HospitalRepositoryImpl implements HospitalRepository {

    private final HospitalPoMapper hospitalPoMapper;

    @Autowired
    public HospitalRepositoryImpl(@Lazy HospitalPoMapper hospitalPoMapper) {
        this.hospitalPoMapper = hospitalPoMapper;
    }

    @Override
    public Hospital load(Long id) {
        HospitalPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到此医院");
        }
        return HospitalConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Hospital aggregate, boolean isNew) {
        HospitalPo po = HospitalConverter.INSTANCE.toPo(aggregate);

        // 新增
        if (isNew) {
            if (hospitalPoMapper.insert(po) <= 0) {
                throw new InternalServerException(String.format("医院 `%s` 新增失败", aggregate.getHospitalName()));
            }

            return po.getCityId();
        }

        // 更新
        if (hospitalPoMapper.update(po) <= 0) {
            throw new InternalServerException(String.format("医院 `%s` 信息更新失败", aggregate.getHospitalName()));
        }

        return po.getId();
    }

    /**
     * 获取持久化对象
     *
     * @param id 医院ID
     * @return 持久化对象
     */
    private HospitalPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return hospitalPoMapper.findById(id);
    }

}
