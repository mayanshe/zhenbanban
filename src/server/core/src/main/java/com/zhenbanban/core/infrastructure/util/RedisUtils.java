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

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 类型 : RedisUtils
 *
 * @author zhangxihai 2025/8/4
 */
@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期事件
     *
     * @param key 键
     * @return 过期时间(单位 : 秒)
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断是否存在为Key的缓存
     *
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 对象
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取指定类型缓存
     *
     * @param key          键
     * @param clazz        类
     * @param defaultValue 找不到的默认值
     * @param <T>          泛型
     * @return 指定类型对象
     */
    public <T> T get(String key, Class<T> clazz, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }

        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return defaultValue;
        }

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return defaultValue;
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     * @return 设置结果
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @return 设置结果
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 数字递增
     *
     * @param key   键
     * @param delta 递增步幅
     * @return 递增后的长整数
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增步幅必须大于0");
        }

        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 数字递减
     *
     * @param key   键
     * @param delta 递剪步幅
     * @return 递增后的长整数
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减步幅必须大于0");
        }

        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 或HSET项值
     *
     * @param key  键
     * @param item 项
     * @return 设置结果
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取制定类型的HSET项值
     *
     * @param key          键
     * @param item         项
     * @param clazz        类
     * @param defaultValue 默认值
     * @param <T>          泛型
     * @return 指定类型项目值
     */
    public <T> T hget(String key, String item, Class<T> clazz, T defaultValue) {
        if (key == null || item == null) {
            return defaultValue;
        }

        Object value = hget(key, item);

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return defaultValue;
    }

    /**
     * 获取HSET下所有键值对
     *
     * @param key 键
     * @return 键值对Map
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置HSet
     *
     * @param key 键
     * @param map Map
     * @return 设置结果
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置HSet并设置时间
     *
     * @param key  键
     * @param map  map
     * @param time 过期时间
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置HSet值
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return 设置结果
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置HSet值 并设置过期时间
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  过期时间
     * @return 设置结果
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量删除hset中的项
     *
     * @param key   键
     * @param items 项
     */
    public void hdel(String key, Object... items) {
        redisTemplate.opsForHash().delete(key, items);
    }

    /**
     * 判断Hset中是否存在指定项
     *
     * @param key  键
     * @param item 项
     * @return
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hset指定项递增
     *
     * @param key  键
     * @param item 项
     * @param by   递增步幅
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hset指定项递减
     *
     * @param key  键
     * @param item 项
     * @param by   递减步幅
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取Set值集合
     *
     * @param key 键
     * @return 获取结果
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询Set中是否存在某个值
     *
     * @param key   键
     * @param value 值
     * @return 是否存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量将值放入Set中
     *
     * @param key    键
     * @param values 值
     * @return 设置结果
     */
    public long sSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 批量将值放入Set中 ， 并设置过期时间
     *
     * @param key    键
     * @param time   过期时间
     * @param values 值
     * @return 设置结果
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sGetSetSize(String key) {
        try {
            Long count = redisTemplate.opsForSet().size(key);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从Set中移除指定值
     *
     * @param key    键
     * @param values 值
     * @return 删除结果
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取指定范围List
     *
     * @param key
     * @param start 开始
     * @param end   结束  0到-1表示所属有值
     * @return 结果
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * 获取List的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        try {
            Long count = redisTemplate.opsForList().size(key);

            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取List指定索引中值
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取List指定索引中指定类型值
     *
     * @param key          键
     * @param index        索引
     * @param clazz        指定类型
     * @param defaultValue 默认值
     * @param <T>          泛型
     * @return 指定类型值
     */
    public <T> T lGetIndex(String key, long index, Class<T> clazz, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }

        Object value = lGetIndex(key, index);
        if (value == null) {
            return defaultValue;
        }

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return defaultValue;
    }

    /**
     * 向List中添加值
     *
     * @param key   键
     * @param value 值
     * @return 是否添加成功
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向List中添加值并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @return 是否添加成功
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 整体设置List
     *
     * @param key   键
     * @param value 值
     * @return 是否添加成功
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 整体设置List并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @return 是否添加成功
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新指定索引List中的值
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除List中值为value的元素
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove == null ? 0 : remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 向有序集合添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 添加结果
     */
    public Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取有序集合成员数
     *
     * @param key 键
     * @return 成员数
     */
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }


    /**
     * 设置有序集合
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 开启事务
     */
    public void multi() {
        redisTemplate.multi();
    }

    /**
     * 执行事务
     *
     * @return 事务执行结果
     */
    public List<Object> exec() {
        return redisTemplate.exec();
    }

    /**
     * 放弃事务
     */
    public void discard() {
        redisTemplate.discard();
    }

}
