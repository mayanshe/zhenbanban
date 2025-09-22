import axios from 'axios'
import type { Pager, ValueObject } from '@/api/common'

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
}

export interface DepartmentView {
  id: string
  parentId: string
  departmentType: ValueObject
  departmentName: string
  summary: string
  description: string
  createdAt: string
  updatedAt: string
  delectedAt: string
  children: DepartmentView[]
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
  return axios.get<DepartmentView>(`/internet-hospital/departments/${id}`)
}

/**
 * 获取所有科室
 */
export function getDepartmentList(data: DepartmentSearchModel) {
  return axios.get<Department[]>('/internet-hospital/departments', { params: { ...data } })
}

/**
 * 添加科室
 * @param data
 */
export function createDepartment(data: Department) {
  return axios.post('/internet-hospital/departments', data)
}

/**
 * 修改科室信息
 * @param data
 */
export function updateDepartment(data: Department) {
  return axios.put(`/internet-hospital/departments/${data.id}`, data)
}

/**
 * 删除科室
 * @param id
 */
export function deleteDepartment(id: string) {
  return axios.delete(`/internet-hospital/departments/${id}`)
}

/**
 * 获取科室类型
 */
export function getDepartmentTypes() {
  return axios.get<ValueObject[]>('/internet-hospital/departments/types')
}
