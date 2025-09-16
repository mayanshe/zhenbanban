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
package com.zhenbanban.bossapi.controller;

import com.zhenbanban.core.application.dto.RegionDto;
import com.zhenbanban.core.application.dto.RegionQuery;
import com.zhenbanban.core.application.query.RegionListQueryHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller : 中国行政区划
 *
 * @author zhangxihai 2025/9/16
 */
@RestController
@RequestMapping("/regions")
public class RegionController {
    private final RegionListQueryHandler regionListQueryHandler;

    public RegionController(@Lazy RegionListQueryHandler regionListQueryHandler) {
        this.regionListQueryHandler = regionListQueryHandler;
    }

    /**
     * 获取行政区划列表
     *
     * @param parentId
     * @return
     */
    @GetMapping
    public List<RegionDto> getRegionList(@RequestParam(value = "parentId", required = false, defaultValue = "0") Long parentId) {
        return regionListQueryHandler.handle(new RegionQuery(parentId));
    }

}
