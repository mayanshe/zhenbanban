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

import lombok.*;

/**
 * Po: 事件
 *
 * @author zhangxihai 2025/08/01
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventPo {
    private Long id;                     // ID

    private Long refId;                  // 关联ID（如用户ID、订单ID等）

    private String eventType;            // 事件类型

    private String eventId;              // 事件ID（唯一标识）

    private String eventData;            // 事件数据

    private Long occurredAt;             // 事件发生时间戳

    @Builder.Default
    private Short state = 0;             // 事件状态: 0-未处理, 1-已处理, 2-已取消

    private String createdBy;            // 创建者ID

    private Long version;                // 版本号，用于乐观锁控制

    @Builder.Default
    private long createdAt = 0L;         // 创建时间戳

    @Builder.Default
    private long updatedAt = 0L;         // 更新时间戳

}

