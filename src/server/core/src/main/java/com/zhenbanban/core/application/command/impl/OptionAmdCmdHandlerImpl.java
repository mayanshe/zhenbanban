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
package com.zhenbanban.core.application.command.impl;

import com.zhenbanban.core.application.command.OptionAmdCmdHandler;
import com.zhenbanban.core.application.dto.OptionAmdCommand;
import com.zhenbanban.core.domain.common.DomainEventPublisher;
import com.zhenbanban.core.domain.systemcontext.entity.Option;
import com.zhenbanban.core.domain.systemcontext.respository.OptionRepository;
import com.zhenbanban.core.infrastructure.util.CacheKeyGenerator;
import com.zhenbanban.core.infrastructure.util.RedisUtils;
import com.zhenbanban.core.shared.contract.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command Achieve : 系统配置选项保存处理器实现类
 *
 * @author zhangxihai 2025/8/24
 */
@Service
@RequiredArgsConstructor
public class OptionAmdCmdHandlerImpl implements OptionAmdCmdHandler {
    private final OptionRepository optionRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final IdGenerator idGenerator;

    private final RedisUtils redisUtils;
    @Override
    @Transactional
    public Long handleAdd(OptionAmdCommand cmd) {
        Long id = idGenerator.nextId();

        Option option = (new ModelMapper()).map(cmd, Option.class);
        option.setId(id);
        option.add();

        optionRepository.save(option, true);
        domainEventPublisher.publish(option.getEvents());

        return id;
    }

    @Override
    @Transactional
    public void handleModify(OptionAmdCommand cmd) {
        Option option = optionRepository.load(cmd.getId());

        option.setOptionName(cmd.getOptionName());
        option.setDisplayName(cmd.getDisplayName());
        option.setOptionValue(cmd.getOptionValue());
        option.setDescription(cmd.getDescription());
        option.setCustomized(cmd.isCustomized());
        option.modify();

        optionRepository.save(option, false);
        domainEventPublisher.publish(option.getEvents());
    }

    @Override
    @Transactional
    public void handleDestroy(Long id) {
        Option option = optionRepository.load(id);
        option.destroy();

        optionRepository.save(option, false);
        domainEventPublisher.publish(option.getEvents());
    }

    @Override
    @Transactional
    public void handleModifyFixedOption(OptionAmdCommand command) {
        boolean isNew = false;

        Option option = optionRepository.loadByOptionName(command.getOptionName());
        if (option == null) {
            isNew = true;
            option = new Option();
            command.setId(idGenerator.nextId());
        } else {
            command.setId(option.getId());
        }

        (new ModelMapper()).map(command, option);
        option.setCustomized(false);

        optionRepository.save(option, isNew);
        domainEventPublisher.publish(option.getEvents());

        if ("cos-setting".equals(command.getOptionName())) {
            clearCosSettingCache();
        }
    }

    /**
     * 清除COS配置缓存
     */
    private void clearCosSettingCache() {
        String settingCacheKey = CacheKeyGenerator.getCosSettingKey();
        String credentialsCacheKey = CacheKeyGenerator.getCosCredentialsKey();
        redisUtils.del(settingCacheKey);
        redisUtils.del(credentialsCacheKey);
    }

}
