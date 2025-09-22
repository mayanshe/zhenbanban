import axios from 'axios'
import { Pager, ValueObject } from '@/api/common'

// 业务医院 Hospital

/**
 * 业务医院交互模型
 */
export interface Hospital {
  id: string
  ownershipType: string
  hospitalType: string
  hospitalLevel: string
  status: string
  insuranceCode: string
  usccCode: string
  hospitalCode: string
  hospitalName: string
  provinceId: string
  province: string
  cityId: string
  city: string
  countyId: string
  county: string
  address: string
  postalCode: string
  longitude: number
  latitude: number
  mapUrl: string
  contactPhone: string
  contactEmail: string
  website: string
  summary: string
  description: string
  companionDiagnosisEnabled: boolean
  mealServiceEnabled: boolean
  testingDeliveryEnabled: boolean
}

/**
 * 业务医院视图模型
 */
export interface HospitalView {
  id: string
  ownershipType: ValueObject
  hospitalType: ValueObject
  hospitalLevel: ValueObject
  status: ValueObject
  insuranceCode: string
  usccCode: string
  hospitalCode: string
  hospitalName: string
  provinceId: string
  province: string
  cityId: string
  city: string
  countyId: string
  county: string
  address: string
  postalCode: string
  longitude: number
  latitude: number
  mapUrl: string
  contactPhone: string
  contactEmail: string
  website: string
  summary: string
  description: string
  companionDiagnosisEnabled: boolean
  mealServiceEnabled: boolean
  testingDeliveryEnabled: boolean
  createdAt: string
  updatedAt: string
  deletedAt: string
}

/**
 * 业务医院搜索模型
 */
export interface HospitalSearchModel {
  keywords: string
  hospitalCode: string
  deleted: boolean
}

/**
 * 获取业务医院
 * @param id
 */
export function getHospital(id: string) {
  return axios.get<HospitalView>(`/hospitals/${id}`)
}

/**
 * 搜索业务医院分页
 * @param data
 * @param page
 */
export function getHospitalPagination(data: HospitalSearchModel, page: Pager) {
  return axios.get<HospitalView>('/hospitals', { params: { ...data, ...page } })
}

/**
 * 添加业务医院
 * @param data
 */
export function createHospital(data: Hospital) {
  return axios.post('/hospitals', data)
}

/**
 * 修改业务医院信息
 * @param data
 */
export function updateHospital(data: Hospital) {
  return axios.put(`/hospitals/${data.id}`, data)
}

/**
 * 删除业务医院
 * @param id
 */
export function deleteHospital(id: string) {
  return axios.delete(`/hospitals/${id}`)
}

/**
 * 获取医院性质列表
 */
export function getHospitalLevelList() {
  return axios.get('/hospitals/levels')
}

/**
 * 获取医院所有制类型列表
 */
export function getHospitalOwnershipTypeList() {
  return axios.get('/hospitals/ownership-types')
}

/**
 * 获取医院类型列表
 */
export function getHospitalTypeList() {
  return axios.get('/hospitals/types')
}
