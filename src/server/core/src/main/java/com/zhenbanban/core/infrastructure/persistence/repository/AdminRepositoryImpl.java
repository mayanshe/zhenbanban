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

import com.zhenbanban.core.domain.accountcontext.entity.Admin;
import com.zhenbanban.core.domain.accountcontext.repository.AdminRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.AdminConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.AccountPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.AdminPoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.AdminRolePoMapper;
import com.zhenbanban.core.infrastructure.persistence.mapper.RolePoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.AdminPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.infrastructure.util.PrintUtils;
import com.zhenbanban.core.infrastructure.util.StrUtils;
import com.zhenbanban.core.shared.exception.BadRequestException;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import com.zhenbanban.core.shared.exception.ServiceUnavailableException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * 类型 : AdminRepositoryImpl
 *
 * @author zhangxihai 2025/8/1
 */
@Repository
public class AdminRepositoryImpl implements AdminRepository {

    private final AdminPoMapper adminPoMapper;

    private final AdminRolePoMapper adminRolePoMapper;

    private final AccountPoMapper accountPoMapper;

    private final RolePoMapper rolePoMapper;

    public AdminRepositoryImpl(
            @Lazy AdminPoMapper adminPoMapper,
            @Lazy AdminRolePoMapper adminRolePoMapper,
            @Lazy AccountPoMapper accountPoMapper,
            @Lazy RolePoMapper rolePoMapper
    ) {
        this.adminPoMapper = adminPoMapper;
        this.adminRolePoMapper = adminRolePoMapper;
        this.accountPoMapper = accountPoMapper;
        this.rolePoMapper = rolePoMapper;
    }

    @Override
    public Admin load(Long id) {
        AdminPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到此管理员");
        }

        return AdminConverter.INSTANCE.toAggregate(po);
    }

    @Override
    public Admin LoadByAccount(String account) {
        if (account == null || account.isBlank()) {
            return null;
        }

        AdminPo po;
        if (StrUtils.isValidChinesePhoneNumber(account)) {
            po = adminPoMapper.findByPhone(account);
        } else if (StrUtils.isValidEmail(account)) {
            po = adminPoMapper.findByEmail(account);
        } else {
            po = adminPoMapper.findByUsername(account);
        }

        return po == null ? null : AdminConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Admin aggregate, boolean isNew) {
        // 禁用
        if (aggregate.isDeleted()) {
            checkSuperAdminCount(aggregate);

            if (adminPoMapper.forbid(aggregate.getId()) <= 0) {
                adminRolePoMapper.deleteByAdminId(aggregate.getId());
                throw new InternalServerException("禁用管理员失败");
            }

            return aggregate.getId();
        }

        if (!accountPoMapper.existsById(aggregate.getId())) {
            throw new BadRequestException("账号不存在");
        }

        AdminPo po = getPo(aggregate.getId());

        // 新增
        if (isNew) {
            if (adminPoMapper.existsById(aggregate.getId())) {
                throw new BadRequestException("管理员已存在");
            }

            po = AdminConverter.INSTANCE.toPo(aggregate);

            if (adminPoMapper.insert(po) <= 0) {
                throw new InternalServerException("新增管理员失败");
            }

            return po.getId();
        }

        // 更新角色
        if (aggregate.isRoleChanged()) {
            adminRolePoMapper.deleteByAdminId(po.getId());
            verifyRolesExists(aggregate.getRoleIds());

            Map<String, Object> params = Map.of(
                    "adminId", po.getId(),
                    "roleIds", new ArrayList<>(aggregate.getRoleIds())
            );

            if (adminRolePoMapper.insert(params) <= 0) {
                throw new ServiceUnavailableException("分配角色失败");
            }

            int count = adminRolePoMapper.countSuperAdmin();
            if (count < 1) {
                throw new BadRequestException("系统应至少保留一个超级管理员账号");
            }

            return po.getId();
        }

        // 更新
        po = AdminConverter.INSTANCE.updatePo(aggregate, po);
        if (adminPoMapper.update(po) <= 0) {
            throw new ServiceUnavailableException("更新管理员信息失败");
        }

        return po.getId();
    }

    /**
     * 验证角色是否存在
     *
     * @param roleIds 角色ID集合
     */
    private void verifyRolesExists(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        int existingCount = rolePoMapper.countByIds(roleIds);
        if (existingCount != roleIds.size()) {
            throw new BadRequestException("部分角色不存在，请检查");
        }
    }

    /**
     * 检查超级管理员数量, 确保至少保留一个超级管理员账号。
     *
     * @param aggregate 管理员聚合根
     */
    private void checkSuperAdminCount(Admin aggregate) {
        if (!aggregate.isSuperAdmin()) {
            return;
        }

        int count = adminRolePoMapper.countSuperAdmin();
        if (count <= 1) {
            throw new BadRequestException("系统应至少保留一个超级管理员账号");
        }
    }

    /**
     * 获取 AdminPo 对象
     *
     * @param id 管理员ID
     * @return AdminPo 对象，如果不存在则返回 null
     */
    private AdminPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return adminPoMapper.findById(id);
    }

}
