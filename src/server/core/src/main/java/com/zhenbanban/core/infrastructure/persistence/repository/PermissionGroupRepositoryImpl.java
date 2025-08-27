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

import com.zhenbanban.core.domain.accountcontext.entity.PermissionGroup;
import com.zhenbanban.core.domain.accountcontext.repository.PermissionGroupRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.PermissionGroupConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionGroupPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.PermissionPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.PermissionGroupPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.RequestConflictException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 领域仓储实现 : 权限分组
 *
 * @author zhangxihai 2025/8/03
 */
@Repository
public class PermissionGroupRepositoryImpl implements PermissionGroupRepository {

    private final PermissionGroupPoMapper permissionGroupPoMapper;

    private final PermissionPoMapper permissionPoMapper;

    @Autowired
    public PermissionGroupRepositoryImpl(
            @Lazy PermissionGroupPoMapper permissionGroupPoMapper,
            @Lazy PermissionPoMapper permissionPoMapper
    ) {
        this.permissionGroupPoMapper = permissionGroupPoMapper;
        this.permissionPoMapper = permissionPoMapper;
    }

    @Override
    public PermissionGroup load(Long id) {
        PermissionGroupPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException(String.format("权限组 '%s' 不存在", id));
        }

        return PermissionGroupConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(PermissionGroup aggregate, boolean isNew) {
        if (aggregate.isDeleted()) {
            if (permissionGroupPoMapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("权限组删除失败");
            }

            List<Long> childrenIds = aggregate.getChildrenIds();
            if (childrenIds != null && !childrenIds.isEmpty()) {
                permissionPoMapper.removeGroup(aggregate.getId());
            }

            return aggregate.getId();
        }

        verify(aggregate);
        PermissionGroupPo po = PermissionGroupConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        // 新增
        if (isNew) {
            if (permissionGroupPoMapper.insert(po) <= 0) {
                throw new InternalServerException("新增权限组失败");
            }

            return po.getId();
        }

        // 更新
        if (permissionGroupPoMapper.update(po) <= 0) {
            throw new InternalServerException("更新权限组信息失败");
        }

        return po.getId();
    }

    /**
     * 验证集
     *
     * @param aggregate 权限组聚合根
     */
    private void verify(PermissionGroup aggregate) {
        // 检查父权限组是否存在
        verifyParentExits(aggregate);

        // 检查权限组名称是否存在
        verifyGroupNameExists(aggregate);

        // 检查权限组显示名称是否存在
        verifyDisplayNameExists(aggregate);
    }

    /**
     * 检查父权限组是否存在
     *
     * @param aggregate 权限组聚合根
     */
    private void verifyParentExits(PermissionGroup aggregate) {
        if (aggregate.getParentId() == null || aggregate.getParentId() <= 0) {
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", aggregate.getParentId());
        int count = permissionGroupPoMapper.count(params);
        if (count <= 0) {
            throw new ResourceNotFoundException(String.format(
                    "父权限组 '%s' 不存在",
                    aggregate.getParentId()
            ));
        }
    }

    /**
     * 检查权限组名称是否存在
     *
     * @param aggregate 权限组聚合根
     */
    private void verifyGroupNameExists(PermissionGroup aggregate) {
        Long existingId = permissionGroupPoMapper.findIdByGroupName(aggregate.getGroupName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format(
                "权限组名称 '%s' 已存在",
                aggregate.getGroupName()
        ));
    }

    /**
     * 检查权限组显示名称是否存在
     *
     * @param aggregate 权限组聚合根
     */
    private void verifyDisplayNameExists(PermissionGroup aggregate) {
        Long existingId = permissionGroupPoMapper.findIdByDisplayName(aggregate.getDisplayName());
        if (existingId == null || existingId.equals(aggregate.getId())) {
            return;
        }

        throw new RequestConflictException(String.format(
                "权限组显示名称 '%s' 已存在",
                aggregate.getDisplayName()
        ));
    }

    /**
     * 获取权限组聚合根
     *
     * @param id 权限组ID
     * @return 权限组Po
     */
    private PermissionGroupPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return permissionGroupPoMapper.findById(id);
    }

}
