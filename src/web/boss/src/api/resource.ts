import axios from 'axios'
import Resource from '@/views/resource/index.vue'

// 资源Resource(菜单/按钮)

/**
 * 资源类型
 */
export interface ResourceType {
  code: string // 资源类型编码
  name: string // 资源类型名称
}

/**
 * 资源
 */
export interface Resource {
  id: string // 资源ID
  parentId: string // 父资源ID
  resourceType: string // 资源类型
  resourceName: string // 资源名称
  displayName: string // 资源显示名称
  description: string // 资源描述
  icon: string // 图标
  url: string // url
  path: string // 路径
  component: string
  sort: number // 排序
  showInMenu: boolean // 是否在菜单中显示
}

/**
 * 资源视图
 */
export interface ResourceView {
  id: string // 资源ID
  parentId: string // 父资源ID
  resourceType: ResourceType // 资源类型
  resourceName: string // 资源名称
  displayName: string // 资源显示名称
  description: string // 资源描述
  icon: string // 图标
  url: string // url
  path: string // 路径
  component: string
  sort: number // 排序
  children: ResourceView[]
  showInMenu: boolean // 是否在菜单中显示
  createdAt: string // 创建时间
  updatedAt: string // 修改时间
}

export interface ResourceButton {
  id: string // 按钮ID
  name: string // 按钮名称
}

export interface ResourceList {
  id: string
  name: string // 显示名称
  path: string // 路径或url
  collapse: boolean // 折叠
  buttons: ResourceButton[] // 按钮列表
  children: ResourceList[]
}

/**
 * 创建资源
 * @param data
 */
export function createResource(data: Resource) {
  return axios.post('/resources', data)
}

/**
 * 更新资源信息
 * @param data
 */
export function updateResource(data: Resource) {
  return axios.put(`/resources/${data.id}`, data)
}

/**
 * 删除资源
 * @param id
 */
export function deleteResource(id: string) {
  return axios.delete(`/resources/${id}`)
}

/**
 * 获取资源类型列表
 */
export function getResourceTypeList() {
  return axios.get<ResourceType[]>('/resources/types')
}

/**
 * 获取资源
 * @param id
 */
export function getResource(id: string) {
  return axios.get<ResourceView>(`/resources/${id}`)
}

/**
 * 获取资源列表
 */
export function getResourceList() {
  return axios.get<ResourceView[]>('/resources')
}
