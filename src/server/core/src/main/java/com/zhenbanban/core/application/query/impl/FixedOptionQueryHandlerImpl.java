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
package com.zhenbanban.core.application.query.impl;

import com.zhenbanban.core.application.query.FixedOptionQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.OptionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.OptionPo;
import com.zhenbanban.core.shared.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

/**
 * Query Achieve : 固定选项查询处理器实现类
 *
 * @author zhangxihai 2025/8/23
 */
@Service
@AllArgsConstructor
public class FixedOptionQueryHandlerImpl implements FixedOptionQueryHandler {
    private final OptionPoMapper mapper;

    @Override
    public <T> T handle(String optionName, String displayName, Class<T> optionType, T defaultValue) {
        OptionPo po = mapper.findByOptionName(optionName);
        if (po == null || po.getOptionValue().isBlank()) {
            return defaultValue;
        }

        // 从配置项中获取值并转换为指定类型
        byte[] bytes = Base64.getDecoder().decode(po.getOptionValue());
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return optionType.cast(ois.readObject());
        } catch (Exception e) {
            throw new BadRequestException(String.format("获取`%s`信息失败", displayName));
        }
    }

}
