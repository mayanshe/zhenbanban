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

import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Converter : 基
 *
 * @author zhangxihai 2025/7/17
 */
public interface IConverter {
    /**
     * 将Short类型转换为Boolean类型
     *
     * @param value
     * @return boolean值，1为true，其他为false
     */
    @Named("shortToBoolean")
    default Boolean mapShortToBoolean(Short value) {
        return value != null && value == 1;
    }

    /**
     * 将Boolean类型转换为Short类型
     *
     * @param value
     * @return Short值，true为1，false为0
     */
    @Named("booleanToShort")
    default Short mapBooleanToShort(Boolean value) {
        return value != null && value ? (short) 1 : (short) 0;
    }

    /**
     * 将isDeleted转换为deletedAt时间戳
     *
     * @param isDeleted 是否已删除
     * @return 时间戳，如果isDeleted为true，则返回当前时间戳，否则返回null
     */
    @Named("isDeletedToDeletedAt")
    default Long mapIsDeletedToDeletedAt(Boolean isDeleted) {
            return isDeleted != null && isDeleted ? System.currentTimeMillis() :    0L;
    }

    /**
     * 将deletedAt时间戳转换为isDeleted
     *
     * @param deletedAt 删除时间
     * @return Boolean值，如果deletedAt不为null，则返回true，否则返回false
     */
    @Named("deletedAtToIsDeleted")
    default Boolean mapDeletedAtToIsDeleted(Long deletedAt) {
        return deletedAt != null && deletedAt > 0L;
    }

    @Named("stringToSet")
    default Set<String> mapStringToSet(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(value.split(",")));
    }

    @Named("setToString")
    default String mapSetToString(Set<String> value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return String.join(",", value);
    }

}
