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
package com.zhenbanban.bossapi.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类型 : SIteInfoRequest
 *
 * @author zhangxihai 2025/8/24
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfoRequest {
    @NotBlank(message = "站点名称不能为空")
    private String siteName;               // 站点名称

    @NotBlank(message = "站点URL不能为空")
    private String siteUrl;                // 站点URL

    @NotBlank(message = "站点描述不能为空")
    private String siteDescription;        // 站点描述

    @NotBlank(message = "站点关键词不能为空")
    private String siteKeywords;           // 站点关键词

    @NotBlank(message = "站点Logo不能为空")
    private String siteLogo;               // 站点Logo URL

    @NotBlank(message = "站点Favicon不能为空")
    private String siteFavicon;            // 站点ICP备案URL

    @Builder.Default
    private String siteIcp = "";                // 站点ICP备案号

}
