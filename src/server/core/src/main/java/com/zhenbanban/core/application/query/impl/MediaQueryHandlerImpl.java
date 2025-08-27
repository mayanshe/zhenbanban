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
package com.zhenbanban.core.application.query.impl;

import com.zhenbanban.core.application.dto.MediaDto;
import com.zhenbanban.core.application.dto.MediaQuery;
import com.zhenbanban.core.application.query.MediaQueryHandler;
import com.zhenbanban.core.infrastructure.persistence.mapper.MediaPoMapper;
import com.zhenbanban.core.infrastructure.persistence.po.MediaPo;
import com.zhenbanban.core.infrastructure.support.paging.Pager;
import com.zhenbanban.core.infrastructure.support.paging.Pagination;
import com.zhenbanban.core.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query Achieve : 媒体资源
 *
 * @author zhangxihai 2025/8/26
 */
@Service
@AllArgsConstructor
public class MediaQueryHandlerImpl implements MediaQueryHandler {
    private final MediaPoMapper mapper;

    @Override
    public MediaDto handleQueryByMd5(String md5) {
        MediaPo po = mapper.findByFileMd5(md5);
        if (po == null) {
            return null;
        }

        return (new ModelMapper()).map(po, MediaDto.class);
    }

    @Override
    public MediaDto handleQuerySingle(Long id) {
        MediaPo po = mapper.findById(id);
        if (po == null) {
            throw new ResourceNotFoundException("未找到此媒体资源");
        }

        return (new ModelMapper()).map(po, MediaDto.class);
    }

    @Override
    public List<MediaDto> handleQueryList(MediaQuery query) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Pagination<MediaDto> handleQueryPage(MediaQuery query) {
        return Pager.paginate(mapper, query.getPage(), query.getPageSize(), query.toMap(), source ->
                (new ModelMapper()).map(source, MediaDto.class));
    }

}
