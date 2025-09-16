import axios from 'axios'
import type { Pager } from '@/api/common'

/**
 * 科室交互模型
 */
export interface Department {
  id: string
  parentId: string
  departmentType: string
  departmentName: string
  summary: string
  description: string
  createdAt: string
  updatedAt: string
}

/**
 * 科室搜索模型
 */
export interface DepartmentSearchModel {
  keywords: string
  departmentType: string
}

/**
 * 获取科室
 * @param id
 */
export function getDepartment(id: string) {
  return axios.get<Department>(`/departments/${id}`)
}

/**
 * 获取所有科室
 */
export function getAllDepartments() {
  return axios.get<Department[]>('/departments/all')
}

/**
 * 搜索科室分页
 * @param data
 * @param page
 */
export function getDepartmentPagination(data: DepartmentSearchModel, page: Pager) {
  return axios.get<Pager<Department>>('/departments', { params: { ...data, ...page } })
}

/**
 * 添加科室
 * @param data
 */
export function createDepartment(data: Department) {
  return axios.post('/departments', data)
}

/**
 * 修改科室信息
 * @param data
 */
export function updateDepartment(data: Department) {
  return axios.put(`/departments/${data.id}`, data)
}

/**
 * 删除科室
 * @param id
 */
export function deleteDepartment(id: string) {
  return axios.delete(`/departments/${id}`)
}
