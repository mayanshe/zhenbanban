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

import com.zhenbanban.core.domain.systemcontext.entity.Media;
import com.zhenbanban.core.domain.systemcontext.respository.MediaRepository;
import com.zhenbanban.core.infrastructure.persistence.converter.MediaConverter;
import com.zhenbanban.core.infrastructure.persistence.mapper.MediaPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.MediaPo;
import com.zhenbanban.core.infrastructure.support.annotation.StoreDomainEventsExecution;
import com.zhenbanban.core.shared.exception.InternalServerException;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repo Achieve : 媒体资源文件
 *
 * @author zhangxihai 2025/8/26
 */
@Repository
public class MediaRepositoryImpl implements MediaRepository {
    private final MediaPoMapper mapper;

    @Autowired
    public MediaRepositoryImpl(@Lazy MediaPoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Media load(Long id) {
        MediaPo po = getPo(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到此媒体资源文件");
        }

        return MediaConverter.INSTANCE.toAggregate(po);
    }

    @Override
    @StoreDomainEventsExecution
    @Transactional
    public Long save(Media aggregate, boolean isNew) {
        // 删除
        if (aggregate.isDeleted()) {
            if (mapper.delete(aggregate.getId()) <= 0) {
                throw new InternalServerException("删除媒体资源文件失败");
            }

            return aggregate.getId();
        }

        MediaPo po = mapper.findByFileMd5(aggregate.getFileMd5());

        if (isNew) {
            po = MediaConverter.INSTANCE.toPo(aggregate);
            if (mapper.insert(po) <= 0) {
                throw new InternalServerException("新增媒体资源文件失败");
            }

            return po.getId();
        }

        po = MediaConverter.INSTANCE.updatePo(aggregate, po);
        if (mapper.update(po) <= 0) {
            throw new InternalServerException("更新媒体资源文件失败");
        }

        return po.getId();
    }

    /**
     * 根据ID获取持久化对象
     *
     * @param id ID
     * @return 持久化对象
     */
    private MediaPo getPo(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return mapper.findById(id);
    }

}
