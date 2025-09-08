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
package com.zhenbanban.core.infrastructure.persistence.mapper;

import com.zhenbanban.core.infrastructure.persistence.po.AntagonismPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mybatis Mapper 接口：十八反十九畏
 *
 * @author zhangxihai 2025/09/03
 */
@Mapper
public interface AntagonismPoMapper extends PaginateMapper<AntagonismPo> {

    int insert(AntagonismPo antagonismPo);

    int update(AntagonismPo antagonismPo);

    int delete(Long id);

    AntagonismPo findById(Long id);

    List<AntagonismPo> findAll();

    Long findIdByPieceIdAndConflictPieceId(@Param("pieceId") Long pieceId, @Param("conflictPieceId") Long conflictPieceId);

}
