import axios from 'axios'

// 权限组PermissionGroup

/**
 * 权限组交互模型
 */
export interface PermissionGroup {
  id: string // 权限组Id
  parentId: string // 父类Id
  groupName: string // 组名称
  displayName: string // 显示名称称
  description: string // 描述
  sort: string // 排序
  createdAt: string // 创建时间
  updatedAt: string // 修改时间
}

/**
 * 权限组显示模型
 */
export interface PermissionGroupView extends PermissionGroup {
  children: PermissionGroup[]
}

/**
 * 权限组搜索Model
 */
export interface PermissionGroupSearchModel {
  parentId: string // 父类Id
  groupName: string // 组名称
}

export interface PermissionGroupKeyValue {
  value: string
  title: string
}

/**
 * 获取权限组实例
 * @param id
 */
export function getPermissionGroup(id: string) {
  return axios.get<PermissionGroup>(`/permission-groups/${id}`)
}

/**
 * 获取全线组列表（树）
 */
export function getPermissionGroupList() {
  return axios.get<PermissionGroupView[]>('/permission-groups')
}

/**
 * 创建权限组
 * @param model
 */
export function createPermissionGroup(model: PermissionGroup) {
  return axios.post('/permission-groups', model)
}

/**
 * 修改权限组
 * @param model
 */
export function updatePermissionGroup(model: PermissionGroup) {
  return axios.put(`/permission-groups/${model.id}`, model)
}

/**
 * 删除权限组
 * @param id
 */
export function deletePermissionGroup(id: string) {
  return axios.delete(`/permission-groups/${id}`)
}
