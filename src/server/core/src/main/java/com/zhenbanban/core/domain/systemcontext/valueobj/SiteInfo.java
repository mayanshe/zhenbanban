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
package com.zhenbanban.core.domain.systemcontext.valueobj;

import lombok.*;

import java.io.Serializable;

/**
 * 类型 : 站点信息
 *
 * @author zhangxihai 2025/8/23
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfo implements Serializable {
    @Builder.Default
    private String siteName = "诊伴伴";                           // 站点名称

    @Builder.Default
    private String siteUrl = "https://zhenbanban.com";           // 站点URL

    @Builder.Default
    private String siteDescription = "互联网医院/伴诊/配餐";        // 站点描述

    @Builder.Default
    private String siteKeywords = "互联网医院,伴诊,配额餐";         // 站点关键词

    @Builder.Default
    private String siteLogo = "";                               // 站点Logo URL

    @Builder.Default
    private String siteFavicon = "";                            // 站点ICP备案URL

    @Builder.Default
    private String siteIcp = "";                                // 站点ICP备案号

    public static SiteInfo defaults() {
        return SiteInfo.builder()
                .build();
    }

}
