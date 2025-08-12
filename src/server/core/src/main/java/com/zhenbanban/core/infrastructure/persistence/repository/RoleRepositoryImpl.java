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

import com.zhenbanban.core.domain.accountcontext.entity.Role;
import com.zhenbanban.core.domain.accountcontext.repository.RoleRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.RoleConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.*;
import com.zhenbanban.core.infrastructure.persistence.po.*;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 领域仓储实现：角色
 *
 * @author zhangxihai 2025/08/01
 */
@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final RolePoMapper roleMapper;

    private final RolePermissionPoMapper rolePermissionMapper;

    private final RoleResourcePoMapper roleResourcePoMapper;

    private final PermissionPoMapper permissionPoMapper;

    private final ResourcePoMapper resourcePoMapper;

    @Autowired
    public RoleRepositoryImpl(
            @Lazy RolePoMapper roleMapper,
            @Lazy RolePermissionPoMapper rolePermissionMapper,
            @Lazy RoleResourcePoMapper roleResourcePoMapper,
            @Lazy PermissionPoMapper permissionPoMapper,
            @Lazy ResourcePoMapper resourcePoMapper
    ) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleResourcePoMapper = roleResourcePoMapper;
        this.permissionPoMapper = permissionPoMapper;
        this.resourcePoMapper = resourcePoMapper;
    }

    @Override
    public Role load(Long id) {
        RolePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException(String.format("角色 '%d' 不存在", id));
        }

        return RoleConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Role aggregate, boolean isNew) {
        // 删除
        if (aggregate.isDeleted()) {
            if (roleMapper.delete(aggregate.getId()) <= 0) {
                rolePermissionMapper.deleteByRoleId(aggregate.getId());
                throw new InternalServerException("角色删除失败");
            }

            return aggregate.getId();
        }

        verify(aggregate);
        RolePo po = RoleConverter.INSTANCE.toPo(aggregate);

        // 新建
        if (isNew) {
            if (roleMapper.insert(po) <= 0) {
                throw new InternalServerException("角色添加失败");
            }

            return po.getId();
        }

        // 更新
        if (roleMapper.update(po) <= 0) {
            throw new InternalServerException("角色更新失败");
        }

        return po.getId();
    }


    @Override
    @StoreDomainEventsExecution
    @Transactional
    public void modifyAssignment(Role aggregate) {
        Long roleId = aggregate.getId();
        Set<Long> permissionIds = aggregate.getPermissionIds();
        Set<Long> resourceIds = aggregate.getResourceIds();

        // 修改权限
        rolePermissionMapper.deleteByRoleId(aggregate.getId());
        if (permissionIds != null && !permissionIds.isEmpty()) {
            if (permissionPoMapper.countByIds(permissionIds) != permissionIds.size()) {
                throw new InternalServerException("部分权限不存在");
            }

            Map<String, Object> permissionParams = Map.of(
                    "roleId", roleId,
                    "permissionIds", permissionIds
            );
            rolePermissionMapper.insert(permissionParams);
        }

        // 修改资源
        roleResourcePoMapper.deleteByRoleId(roleId);
        if (resourceIds != null && !resourceIds.isEmpty()) {
            if (resourcePoMapper.countByIds(resourceIds) != resourceIds.size()) {
                throw new InternalServerException("部分资源不存在");
            }

            Map<String, Object> resourceParams = Map.of(
                    "roleId", roleId,
                    "resourceIds", resourceIds
            );
            roleResourcePoMapper.insert(resourceParams);
        }
    }

    /**
     * 验证集
     *
     * @param aggregate 角色聚合根
     */
    private void verify(Role aggregate) {
        // 验证角色名称是否已存在
        verifyRoleNameExists(aggregate);

        // 验证角色显示名称是否已存在
        verifyDisplayNameExists(aggregate);
    }

    /**
     * 验证角色名称是否已存在
     *
     * @param aggregate 角色聚合根
     */
    private void verifyRoleNameExists(Role aggregate) {
        Long existingId = roleMapper.findIdByRoleName(aggregate.getRoleName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format("角色名称 '%s' 已存在", aggregate.getRoleName()));
    }

    /**
     * 验证角色显示名称是否已存在
     *
     * @param aggregate 角色聚合根
     */
    private void verifyDisplayNameExists(Role aggregate) {
        Long existingId = roleMapper.findByDisplayName(aggregate.getDisplayName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format("角色显示名称 '%s' 已存在", aggregate.getDisplayName()));
    }

    /**
     * 根据ID加载角色持久化对象
     *
     * @param id 角色ID
     * @return 角色持久化对象
     */
    private RolePo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return roleMapper.findById(id);
    }

}
