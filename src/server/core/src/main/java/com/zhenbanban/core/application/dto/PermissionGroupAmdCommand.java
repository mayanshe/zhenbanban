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
package com.zhenbanban.core.application.dto;

import com.zhenbanban.core.application.common.BaseCommand;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 命令载体 : 权限组
 *
 * @author zhangxihai 2025/8/03
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroupAmdCommand extends BaseCommand<Long> {
    private Long parentId;               // 父权限组ID

    private String groupName;            // 权限组名称

    private String displayName;          // 权限组显示名称

    private String description;          // 权限组描述

    @Builder.Default
    private boolean deleted = false;     // 是否已删除，默认未删除

    private int sort;                    // 排序，倒叙


}
