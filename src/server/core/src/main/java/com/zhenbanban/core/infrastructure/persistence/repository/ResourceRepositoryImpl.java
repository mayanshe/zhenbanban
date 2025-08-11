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

import com.zhenbanban.core.domain.accountcontext.entity.Resource;
import com.zhenbanban.core.domain.accountcontext.repository.ResourceRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.ResourceConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.ResourcePoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.RoleResourcePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.ResourcePo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import com.zhenbanban.core.shared.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Domain Repository Achieve : 资源
 *
 * @author zhangxihai 2025/8/03
 */
@Repository
public class ResourceRepositoryImpl implements ResourceRepository {
    private final ResourcePoMapper resourcePoMapper;

    private final RoleResourcePoMapper roleResourcePoMapper;

    @Autowired
    public ResourceRepositoryImpl(
            @Lazy ResourcePoMapper resourcePoMapper,
            @Lazy RoleResourcePoMapper roleResourcePoMapper) {
        this.resourcePoMapper = resourcePoMapper;
        this.roleResourcePoMapper = roleResourcePoMapper;
    }

    @Override
    public Resource load(Long id) {
        ResourcePo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException(String.format("资源ID %d 不存在", id));
        }

        return ResourceConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Resource aggregate, boolean isNew) {
        // 删除
        if (aggregate.isDeleted()) {
            if (resourcePoMapper.delete(aggregate.getId()) <= 0 ) {
                roleResourcePoMapper.deleteByResourceId(aggregate.getId());
                throw new ServiceUnavailableException(String.format("资源 `%d` 删除失败", aggregate.getId()));
            }

            return aggregate.getId();
        }

        // 设置排序
        aggregate.setSort(getSort(aggregate.getParentId(), aggregate.getSort()));
        ResourcePo po = ResourceConverter.INSTANCE.toPo(aggregate);
        po.setId(aggregate.getId());

        // 新增
        if (isNew) {
            if (resourcePoMapper.insert(po) <= 0) {
                throw new ServiceUnavailableException(String.format("资源 `%s` 新增失败", aggregate.getResourceName()));
            }

            return po.getId();
        }

        // 更新
        if (resourcePoMapper.update(po) <= 0) {
            throw new ServiceUnavailableException(String.format("资源 `%s` 更新失败", aggregate.getResourceName()));
        }

        return po.getId();
    }

    /**
     * 验证资源聚合根
     *
     * @param aggregate 资源聚合根
     */
    private void verify(Resource aggregate) {
        // 验证父资源是否存在
        verifyParentExists(aggregate.getParentId());

        // 验证资源名称是否已存在
        verifyResourceNameExists(aggregate);

        // 验证资源显示名称是否已存在
        verifyDisplayNameExists(aggregate);
    }

    /**
     * 获取资源的排序值
     *
     * @param parentId 父资源ID
     * @param sort     自定义排序值
     * @return 排序值
     */
    private int getSort(long parentId, int sort) {
        // 如果自定义sort,则直接返回
        if (sort > 0) {
            return sort;
        }

        // 通过parentId获取最大sort值 + 10
        Integer maxSort = resourcePoMapper.findMaxSortByParentId(parentId);
        if (maxSort == null || maxSort <= 0L) {
            return 10;
        }

        return maxSort + 10;
    }

    /**
     * 验证父资源是否存在
     *
     * @param parentId 父资源ID
     */
    private void verifyParentExists(Long parentId) {
        if (parentId == null || parentId <= 0L) {
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", parentId);
        int count = resourcePoMapper.count(params);
        if (count <= 0) {
            throw new ResourceNotFoundException(String.format("父资源 `%d` 不存在", parentId));
        }
    }

    /**
     * 验证资源名称是否已存在
     *
     * @param aggregate 资源聚合根
     */
    private void verifyResourceNameExists(Resource aggregate) {
        Long exitingId = resourcePoMapper.findIdByResourceName(aggregate.getResourceName());
        if (exitingId == null || exitingId.equals(aggregate.getId())) {
            return;
        }

        throw new ResourceNotFoundException(String.format("资源名称 `%s` 已存在", aggregate.getResourceName()));
    }

    private void verifyDisplayNameExists(Resource aggregate) {
        Long exitingId = resourcePoMapper.findIdByDisplayName(aggregate.getDisplayName());
        if (exitingId == null || exitingId.equals(aggregate.getId())) {
            return;
        }

        throw new ResourceNotFoundException(String.format("资源显示名称 `%s` 已存在", aggregate.getDisplayName()));
    }

    /**
     * 获取资源PO
     *
     * @param id 资源ID
     * @return 资源PO对象
     */
    private ResourcePo getPo(Long id) {
        if (id == null || id <= 0L) {
            return null;
        }

        return resourcePoMapper.findById(id);
    }

}
