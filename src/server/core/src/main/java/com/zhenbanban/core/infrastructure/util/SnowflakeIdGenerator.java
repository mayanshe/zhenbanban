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
package com.zhenbanban.core.infrastructure.util;

import com.zhenbanban.core.shared.contract.IdGenerator;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 工具类：雪花算法实现ID生成器
 *
 * @author zhangxihai 2025/08/01
 */
public class SnowflakeIdGenerator implements IdGenerator {
    private static final long EPOCH = 1735660800000L;             // 2025-01-01 00:00:00 UTC
    private static final int MAX_CLOCK_BACKWARD_MS = 1000;        // 最大允许时钟回拨1秒

    // 各部分的位数配置
    private static final long TIMESTAMP_BITS = 41L;
    private static final long MACHINE_ID_BITS = 10L;              // 机器ID位数 最大1023
    private static final long SEQUENCE_BITS = 12L;                // 序列号位数 最大4095 毫秒

    // 最大值计算
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    // 位移计算
    private static final long SEQUENCE_SHIFT = 0L;
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = MACHINE_ID_SHIFT + MACHINE_ID_BITS;

    private final long machineId;
    private volatile long sequence = 0L;
    private volatile long lastTimestamp = -1L;
    private volatile long clockBackwardCount = 0L;

    public SnowflakeIdGenerator(long machineId) {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException(
                    String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long timestamp = currentTimestamp();

        // 检查时间戳是否早于epoch
        if (timestamp < EPOCH) {
            throw new RuntimeException(
                    String.format("Clock timestamp %d is earlier than epoch %d", timestamp, EPOCH));
        }

        // 处理时钟回拨
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= MAX_CLOCK_BACKWARD_MS) {
                try {
                    wait(offset);
                    timestamp = currentTimestamp();
                    clockBackwardCount++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Clock backward wait interrupted", e);
                }
            } else {
                throw new RuntimeException(
                        String.format("Clock moved backwards. Refusing to generate ID for %d ms", offset));
            }
        }

        // 同一毫秒内序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = ThreadLocalRandom.current().nextLong(0, 10L);       // 随机起始序列号
        }

        lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | (sequence << SEQUENCE_SHIFT);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }
        return timestamp;
    }

    private long currentTimestamp() {
        return System.currentTimeMillis();
    }

    // 监控指标获取方法
    public long getClockBackwardCount() {
        return clockBackwardCount;
    }

}
