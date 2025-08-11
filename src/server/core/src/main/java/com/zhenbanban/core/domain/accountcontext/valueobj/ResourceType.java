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
package com.zhenbanban.core.domain.accountcontext.valueobj;

import com.zhenbanban.core.domain.accountcontext.entity.Resource;
import com.zhenbanban.core.shared.exception.BadRequestException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value : 资源类型
 *
 * @author zhangxihai 2025/8/03
 */
public class ResourceType {
    public static final ResourceType MENU = new ResourceType("menu", "菜单");
    public static final ResourceType BUTTON = new ResourceType("button", "按钮");
    public static final ResourceType LINK = new ResourceType("link", "链接");
    public static final ResourceType COMPONENT = new ResourceType("component", "组件");
    private static final List<ResourceType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
            MENU,
            BUTTON,
            LINK,
            COMPONENT
    ));

    private final String code;       // 资源类型代码

    private final String name;       // 资源类型名称

    public ResourceType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 获取资源类型代码
     *
     * @return 资源类型代码
     */
    public static List<ResourceType> all() {
        return ALL_TYPES;
    }

    public static ResourceType of(String code) {
        if (code == null || code.isEmpty()) {
            return MENU;
        }

        return switch (code) {
            case "MENU" -> MENU;
            case "BUTTON" -> BUTTON;
            case "LINK" -> LINK;
            case "COMPONENT" -> COMPONENT;
            default -> MENU;
        };
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceType that)) return false;
        return code.equals(that.code) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ResourceType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
