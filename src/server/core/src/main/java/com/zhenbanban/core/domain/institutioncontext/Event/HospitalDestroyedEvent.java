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
package com.zhenbanban.core.domain.institutioncontext.Event;

import com.zhenbanban.core.domain.common.AbsDomainEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 事件 : 医院添加成功事件
 *
 * @author zhangxihai 2025/8/11
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDestroyedEvent extends AbsDomainEvent {
    private Long hospitalId;                                  // 医院ID

    private String insuranceCode;                             // 医保编码（唯一标识）

    private String hospitalCode;                              // 医院编码（唯一标识）

    private String hospitalName;                              // 医院名称

}
