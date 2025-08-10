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
package com.zhenbanban.core.shared.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 共享拦截器：自动给表行设置createdAt和updatedAt
 *
 * @author zhangxihai 2025/08/02
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class TimestampInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];

            // 获取SQL命令类型
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

            if (parameter != null) {
                // 获取参数对象的MetaObject
                MetaObject metaObject = SystemMetaObject.forObject(parameter);

                // 获取当前时间戳(毫秒级)
                long currentTimestamp = System.currentTimeMillis();

                if (sqlCommandType == SqlCommandType.INSERT) {
                    // 插入操作设置createdAt和updatedAt
                    if (metaObject.hasGetter("createdAt")) {
                        metaObject.setValue("createdAt", currentTimestamp);
                    }
                    if (metaObject.hasGetter("updatedAt")) {
                        metaObject.setValue("updatedAt", currentTimestamp);
                    }
                } else if (sqlCommandType == SqlCommandType.UPDATE) {
                    // 更新操作只设置updatedAt
                    if (metaObject.hasGetter("updatedAt")) {
                        metaObject.setValue("updatedAt", currentTimestamp);
                    }
                }
            }
        } catch (Exception ignore) {}

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
