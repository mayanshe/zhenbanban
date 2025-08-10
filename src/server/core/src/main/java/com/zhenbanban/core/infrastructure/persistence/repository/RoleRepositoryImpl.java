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
import com.zhenbanban.core.infrastructure.persistence.mapper.RolePermissionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.RolePoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.RoleResourcePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.*;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
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

    @Autowired
    public RoleRepositoryImpl(
            @Lazy RolePoMapper roleMapper,
            @Lazy RolePermissionPoMapper rolePermissionMapper,
            @Lazy RoleResourcePoMapper roleResourcePoMapper
    ) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleResourcePoMapper = roleResourcePoMapper;
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
        saveAssignment(aggregate, po);
        if (roleMapper.update(po) <= 0) {
            throw new InternalServerException("角色更新失败");
        }

        return po.getId();
    }

    /**
     * 保存角色与权限、资源的关联
     *
     * @param aggregate 角色聚合根
     * @param po        角色持久化对象
     */
    private void saveAssignment(Role aggregate, RolePo po) {
        Long roleId = po.getId();
        List<PermissionPo> permissions = po.getPermissions();
        List<ResourcePo> resources = po.getResources();

        Set<Long> oldPermissionIds = permissions == null ? Set.of() :
                permissions.stream().map(PermissionPo::getId).collect(Collectors.toSet());
        Set<Long> oldResourceIds = resources == null ? Set.of() :
                resources.stream().map(ResourcePo::getId).collect(Collectors.toSet());

        List<RolePermissionPo> insetRolePermissionPos = List.of();
        List<RolePermissionPo> deleteRolePermissionPos = List.of();
        List<RoleResourcePo> insertRoleResourcePos = List.of();
        List<RoleResourcePo> deleteRoleResourcePos = List.of();

        geTRolePermissionChange(
                roleId,
                oldPermissionIds,
                aggregate.getPermissionIds(),
                insetRolePermissionPos,
                deleteRolePermissionPos
        );

        getRoleResourceChange(
                roleId,
                oldResourceIds,
                aggregate.getResourceIds(),
                insertRoleResourcePos,
                deleteRoleResourcePos
        );

        // 添加权限绑定
        if (!insertRoleResourcePos.isEmpty()) {
            if (rolePermissionMapper.batchInsert(insetRolePermissionPos) <= 0) {
                throw new InternalServerException("角色权限/菜单修改失败");
            }
        }

        // 删除权限绑定
        if (!deleteRolePermissionPos.isEmpty()) {
            if (rolePermissionMapper.batchDelete(deleteRolePermissionPos) <= 0) {
                throw new InternalServerException("角色权限/菜单修改失败");
            }
        }

        // 添加资源绑定
        if (!insertRoleResourcePos.isEmpty()) {
            if (roleResourcePoMapper.batchInsert(insertRoleResourcePos) <= 0) {
                throw new InternalServerException("角色权限/菜单修改失败");
            }
        }

        // 删除资源绑定
        if (!deleteRoleResourcePos.isEmpty()) {
            if (roleResourcePoMapper.batchDelete(deleteRoleResourcePos) <= 0) {
                throw new InternalServerException("角色资源修改失败");
            }
        }
    }

    /**
     * 获取角色权限变更
     *
     * @param roleId
     * @param oldPermissionIds
     * @param newPermissionIds
     * @param insetRolePermissionPos
     * @param deleteRolePermissionPos
     */
    private void geTRolePermissionChange(
            Long roleId,
            Set<Long> oldPermissionIds,
            Set<Long> newPermissionIds,
            List<RolePermissionPo> insetRolePermissionPos,
            List<RolePermissionPo> deleteRolePermissionPos

    ) {
        Set<Long> deletePermissionIds = new HashSet<>(oldPermissionIds);
        Set<Long> insertPermissionIds = new HashSet<>(newPermissionIds);
        deletePermissionIds.removeAll(newPermissionIds);
        insertPermissionIds.removeAll(oldPermissionIds);

        if (!deletePermissionIds.isEmpty()) {
            for (long permissionId : deletePermissionIds) {
                deleteRolePermissionPos.add(
                        RolePermissionPo.builder()
                                .roleId(roleId)
                                .permissionId(permissionId)
                                .build()
                );
            }
        }

        if (!insertPermissionIds.isEmpty()) {
            for (long permissionId : insertPermissionIds) {
                insetRolePermissionPos.add(
                        RolePermissionPo.builder()
                                .roleId(roleId)
                                .permissionId(permissionId)
                                .createdAt(System.currentTimeMillis())
                                .build()
                );
            }
        }
    }

    /**
     * 获取角色资源变更
     *
     * @param roleId
     * @param oldResourceIds
     * @param newResourceIds
     * @param insertRoleResourcePos
     * @param deleteRoleResourcePos
     */
    private void getRoleResourceChange(
            Long roleId,
            Set<Long> oldResourceIds,
            Set<Long> newResourceIds,
            List<RoleResourcePo> insertRoleResourcePos,
            List<RoleResourcePo> deleteRoleResourcePos
    ) {
        Set<Long> deleteResourceIds = new HashSet<>(oldResourceIds);
        Set<Long> insertResourceIds = new HashSet<>(newResourceIds);
        deleteResourceIds.removeAll(newResourceIds);
        insertResourceIds.removeAll(oldResourceIds);

        if (!deleteResourceIds.isEmpty()) {
            for (long resourceId : deleteResourceIds) {
                deleteRoleResourcePos.add(
                        RoleResourcePo.builder()
                                .roleId(roleId)
                                .resourceId(resourceId)
                                .build()
                );
            }
        }

        if (!insertResourceIds.isEmpty()) {
            for (long resourceId : insertResourceIds) {
                insertRoleResourcePos.add(
                        RoleResourcePo.builder()
                                .roleId(roleId)
                                .resourceId(resourceId)
                                .createAt(System.currentTimeMillis())
                                .build()
                );
            }
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
