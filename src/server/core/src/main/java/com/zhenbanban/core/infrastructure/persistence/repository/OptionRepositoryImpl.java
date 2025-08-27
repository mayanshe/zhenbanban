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

import com.zhenbanban.core.domain.systemcontext.entity.Option;
import com.zhenbanban.core.domain.systemcontext.respository.OptionRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.OptionConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.OptionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.OptionPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.BadRequestException;
import com.zhenbanban.core.shared.exception.InternalServerException;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.Optional;

/**
 * Repo Achieve : 系统配置选项
 *
 * @author zhangxihai 2025/8/24
 */
@Repository
public class OptionRepositoryImpl implements OptionRepository {
    private final OptionPoMapper mapper;

    @Autowired
    public OptionRepositoryImpl(OptionPoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Option load(Long id) {
        OptionPo po = getPo(id);
        if (po == null) {
            throw new ResolutionException("未找到此系统配置选项");
        }

        return OptionConverter.INSTANCE.toAggregate(po);
    }

    @Override
    public Option loadByOptionName(String optionName) {
        OptionPo po = mapper.findByOptionName(optionName);
        if (po == null) {
            return null;
        }

        return OptionConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Option aggregate, boolean isNew) {
        // 删除
        if (aggregate.isDeleted()) {
            if (mapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除系统配置选项失败");
            }

            return aggregate.getId();
        }

        verify(aggregate);
        OptionPo po = OptionConverter.INSTANCE.toPo(aggregate);

        if (isNew) {
            if (mapper.insert(po) <= 0) {
                throw new InternalServerException("保存系统配置选项失败");
            }

            return po.getId();
        }

        if (mapper.update(po) <= 0) {
            throw new InternalServerException("更新系统配置选项失败");
        }

        return po.getId();
    }

    /**
     * 验证系统配置选项聚合
     *
     * @param aggregate 系统配置选项聚合
     */
    private void verify(Option aggregate) {
        verifyOptionNameExists(aggregate);
        verifyDisplayNameExists(aggregate);
    }

    /**
     * 验证系统配置选项名称是否已存在
     *
     * @param aggregate 系统配置选项聚合
     */
    private void verifyOptionNameExists(Option aggregate) {
        Long existingId = mapper.findIdByOptionName(aggregate.getOptionName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new BadRequestException(String.format("系统配置选项名称 '%s' 已存在", aggregate.getOptionName()));
    }

    /**
     * 验证系统配置选项显示名称是否已存在
     *
     * @param aggregate 系统配置选项聚合
     */
    private void verifyDisplayNameExists(Option aggregate) {
        Long existingId = mapper.findIdByDisplayName(aggregate.getDisplayName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new BadRequestException(String.format("系统配置选项显示名称 '%s' 已存在", aggregate.getDisplayName()));
    }

    /**
     * 加载系统配置选项
     *
     * @param id 系统配置选项ID
     * @return OptionPo
     */
    private OptionPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return mapper.findById(id);
    }

}
