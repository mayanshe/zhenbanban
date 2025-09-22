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
package com.zhenbanban.core.infrastructure.persistence.po;

import com.zhenbanban.core.infrastructure.support.annotation.InList;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Po: 互联网医院科室表
 *
 * @author zhangxihai 2025/09/16
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentPo {
    private Long id;                                      // 科室ID

    private Long parentId;                                // 上级科室ID

    private String departmentType;                        // 科室类型

    private String departmentName;                        // 科室名称

    private String summary;                               // 科室简介

    private String description;                           // 科室介绍

    @Builder.Default
    private long createdAt = 0L;                          // 创建时间

    @Builder.Default
    private long updatedAt = 0L;                          // 更新时间

    @Builder.Default
    private Long deletedAt = 0L;                          // 删除时间，0表示未删除

    @Builder.Default
    private List<DepartmentPo> children = List.of();      // 子科室列表

}
