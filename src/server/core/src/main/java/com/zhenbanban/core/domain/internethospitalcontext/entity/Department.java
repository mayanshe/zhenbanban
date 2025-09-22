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
package com.zhenbanban.core.domain.internethospitalcontext.entity;

import com.zhenbanban.core.domain.common.entity.AbsAggregate;
import com.zhenbanban.core.domain.internethospitalcontext.event.DepartmentAddedEvent;
import com.zhenbanban.core.domain.internethospitalcontext.event.DepartmentDestroyedEvent;
import com.zhenbanban.core.domain.internethospitalcontext.event.DepartmentModifiedEvent;
import lombok.*;

/**
 * 聚合根: 互联网医院科室
 *
 * @author zhangxihai 2025/09/16
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Department extends AbsAggregate {
    private Long id;
    private Long parentId;
    private String departmentType;
    private String departmentName;
    private String summary;
    private String description;
    @Builder.Default
    private boolean deleted = false;      // 是否已删除

    /**
     * 添加科室
     */
    public void add() {
        this.setDeleted(false);

        DepartmentAddedEvent event = DepartmentAddedEvent.builder()
                .refId(this.getId())
                .departmentId(this.getId())
                .departmentName(this.getDepartmentName())
                .build();

        this.addEvent(event);
    }

    /**
     * 修改科室
     */
    public void modify() {
        this.setDeleted(false);

        DepartmentModifiedEvent event = DepartmentModifiedEvent.builder()
                .refId(this.getId())
                .departmentId(this.getId())
                .departmentName(this.getDepartmentName())
                .build();

        this.addEvent(event);
    }

    /**
     * 销毁科室
     */
    public void destroy() {
        this.setDeleted(true);

        DepartmentDestroyedEvent event = DepartmentDestroyedEvent.builder()
                .refId(this.getId())
                .departmentId(this.getId())
                .departmentName(this.getDepartmentName())
                .build();

        this.addEvent(event);
    }

}
