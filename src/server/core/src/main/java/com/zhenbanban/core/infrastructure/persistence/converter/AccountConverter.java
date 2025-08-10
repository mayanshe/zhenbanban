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
package com.zhenbanban.core.infrastructure.persistence.converter;

import com.zhenbanban.core.domain.accountcontext.entity.Account;
import com.zhenbanban.core.infrastructure.persistence.po.AccountPo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 转换器 : 账号
 *
 * @author zhangxihai 2025/8/2
 */
@Mapper
public interface AccountConverter extends IConverter {
    AccountConverter INSTANCE = Mappers.getMapper(AccountConverter.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "state", source = "forbidden", qualifiedByName = "forbiddenToState"),
            @Mapping(target = "scopes", source = "scopes", qualifiedByName = "setToString"),
    })
    AccountPo toPo(Account aggregate);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "state", source = "forbidden", qualifiedByName = "forbiddenToState"),
            @Mapping(target = "scopes", source = "scopes", qualifiedByName = "setToString"),
    })
    AccountPo updatePo(Account aggregate, @MappingTarget AccountPo accountPo);

    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "forbidden", source = "state", qualifiedByName = "stateToForbidden"),
            @Mapping(target = "scopes", source = "scopes", qualifiedByName = "stringToSet"),
    })
    Account toAggregate(AccountPo accountPo);

    @Named("stateToForbidden")
    default boolean mapStateToForbidden(short state) {
        return state == 0;
    }

    @Named("forbiddenToState")
    default short mapForbiddenToState(boolean forbidden) {
        return forbidden ? (short) 0 : (short) 10;
    }

}
