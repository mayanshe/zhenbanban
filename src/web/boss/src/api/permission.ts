import axios from 'axios'
import { Pager } from "@/api/common";

// 权限Permission

/**
 * 权限交互模型
 */
export interface Permission {
    id: string // 权限ID
    groupId: string // 权限分组id
    permissionName: string // 权限名称
    displayName: string // 权限显示名称
    description: string // 权限描述
}

/**
 * 权限视图模型
 */
export interface PermissionView extends Permission {
    groupName: string // 权限分组名称
    createdAt: string // 权限创建时间
    updatedAt: string // 权限修改时间
}

/**
 * 权限搜素模型
 */
export interface PermissionSearchModel {
    keywords: string // 关键词
    groupId: string // 关键词
}

/**
 * 获取权限
 * @param id
 */
export function getPermission(id: string) {
    return axios.get(`/permissions/${id}`)
}

/**
 * 搜索权限分页
 * @param data
 */
export function getPermissionPagination(data: PermissionSearchModel, page: Pager) {
    return axios.get('/permissions', {params: {...data, ...page}})
}

/**
 * 添加权限
 * @param data
 */
export function createPermission(data: Permission) {
    return axios.post('/permissions', data)
}

/**
 * 修改权限信息
 * @param data
 */
export function updatePermission(data: Permission) {
    return axios.put(`/permissions/${data.id}`, data)
}

/**
 * 删除权限
 *
 * @param id
 */
export function deletePermission(id: string) {
    return axios.delete(`/permissions/${id}`)
}