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
package com.zhenbanban.core.infrastructure.external.cos;

/**
 * 类型 : CosFile
 *
 * @author zhangxihai 2025/8/25
 */
public class CosFile {
    private String md5;

    private String path;

    private String contentType;

    public CosFile(String md5, String path, String contentType) {
        this.md5 = md5;
        this.path = path;
        this.contentType = contentType;
    }

    public String getMd5() {
        return md5;
    }

    public String getPath() {
        return path;
    }

    public String getContentType() {
        return contentType;
    }

}
