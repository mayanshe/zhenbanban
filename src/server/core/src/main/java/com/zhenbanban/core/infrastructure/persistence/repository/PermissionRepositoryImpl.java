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
package com.zhenbanban.core.infrastructure.persistence.repository;

import com.zhenbanban.core.domain.accountcontext.entity.Permission;
import com.zhenbanban.core.domain.accountcontext.repository.PermissionRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.PermissionConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionGroupPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.RolePermissionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * 领域仓储实现 : 权限
 *
 * @author zhangxihai 2025/8/03
 */
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    private final PermissionPoMapper permissionMapper;

    private final PermissionGroupPoMapper permissionGroupMapper;

    private final RolePermissionPoMapper rolePermissionMapper;

    public PermissionRepositoryImpl(
            @Lazy PermissionPoMapper permissionMapper,
            @Lazy PermissionGroupPoMapper permissionGroupMapper,
            @Lazy RolePermissionPoMapper rolePermissionMapper
    ) {
        this.permissionMapper = permissionMapper;
        this.permissionGroupMapper = permissionGroupMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Permission load(Long id) {
        PermissionPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到对应的权限信息");
        }

        return PermissionConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Permission aggregate, boolean isNew) {
        // 删除
        if (aggregate.isDeleted()) {
            if (permissionMapper.delete(aggregate.getId()) <= 0) {
                rolePermissionMapper.deleteByPermissionId(aggregate.getId());
                throw new ServiceUnavailableException("删除权限失败");
            }

            return aggregate.getId();
        }

        verify(aggregate);
        PermissionPo po = PermissionConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        // 添加
        if (isNew) {
            if (permissionMapper.insert(po) <= 0) {
                throw new ServiceUnavailableException("添加权限失败");
            }

            return po.getId();
        }

        // 更新
        if (permissionMapper.update(po) <= 0) {
            throw new InternalServerException("更新权限失败");
        }

        return po.getId();
    }

    /** 保存权限聚合根
     *
     * @param aggregate 权限聚合根
     */
    private void verify(Permission aggregate) {
        // 验证父权限组是否存在
        verifyPermissionGroupExists(aggregate.getGroupId());

        // 验证权限名称和显示名称是否存在
        verifyPermissionNameExists(aggregate);

        // 验证权限显示名称是否存在
        verifyDisplayNameExists(aggregate);
    }

    /**
     * 验证父权限组是否存在
     *
     * @param groupId 权限分组ID
     */
    private void verifyPermissionGroupExists(Long groupId) {
        if (groupId == null || groupId < 0) {
            throw new BadRequestException("请选择权限分组");
        }

        if (groupId == 0) {
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", groupId);
        if (permissionGroupMapper.count(params) <= 0) {
            throw new ResourceNotFoundException(String.format("权限分组ID '%s' 不存在", groupId));
        }
    }

    /**
     * 验证权限名称是否已存在
     *
     * @param aggregate 权限聚合根
     */
    private void verifyPermissionNameExists(Permission aggregate) {
        Long id = permissionMapper.findIdByPermissionName(aggregate.getPermissionName());
        if (id == null || id.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format("权限名称 '%s' 已存在", aggregate.getPermissionName()));
    }

    /**
     * 验证权限显示名称是否已存在
     *
     * @param aggregate 权限聚合根
     */
    private void verifyDisplayNameExists(Permission aggregate) {
        Long id = permissionMapper.findByDisplayName(aggregate.getDisplayName());
        if (id == null || id.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format("权限显示名称 '%s' 已存在", aggregate.getPermissionName()));
    }

    /**
     * 获取权限持久化对象
     *
     * @param id 权限ID
     * @return 权限持久化对象
     * @throws ResourceNotFoundException 如果未找到对应的权限
     */
    private PermissionPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return permissionMapper.findById(id);
    }

}
