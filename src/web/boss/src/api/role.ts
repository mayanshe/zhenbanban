import axios from 'axios'
import { Pager, Pagination } from '@/api/common'

// 角色 Role

/**
 * 角色
 */
export interface Role {
  id: string
  roleName: string
  displayName: string
  description: string
}

/**
 * 角色视图
 */
export interface RoleView extends Role {
  createdAt: string
  updatedAt: string
  permissionIds: string[]
  resourcesIds: string[]
}

/**
 * 角色搜索模型
 */
export interface RoleSearchModel {
  keywords: string
}

/**
 * 添加角色
 * @param data
 */
export function createRole(data: Role) {
  return axios.post('/roles', data)
}

/**
 * 修改角色信息
 * @param data
 */
export function updateRole(data: Role) {
  return axios.put(`/roles/${data.id}`, data)
}

/**
 * 删除角色
 * @param id
 */
export function deleteRole(id: string) {
  return axios.delete(`/roles/${id}`)
}

/**
 * 获取角色信息
 * @param id
 */
export function getRole(id: string) {
  return axios.get<RoleView>(`/roles/${id}`)
}

/**
 * 获取角色列表分页
 * @param data
 * @param page
 */
export function getRolePagination(data: RoleSearchModel, page: Pager) {
  return axios.get<Pagination<RoleView>>('/roles', { params: { ...data, ...page } })
}
