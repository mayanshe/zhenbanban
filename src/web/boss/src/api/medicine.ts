import axios from 'axios'
import { Pager } from '@/api/common'

// 西药及中成药 Medicine

/**
 * 药品交互模型
 */
export interface Medicine {
  id: string
  medicineCode: string
  medicineName: string
  registeredName: string
  registeredMedicineModel: string
  realityMedicineModel: string
  registeredOutlook: string
  realityOutlook: string
  materialName: string
  factor: number
  unit: string
  minUnit: string
  companyName: string
  approvalCode: string
  standardCode: string
  indication: string
  description: string
  otc: number
  poisonous: number
}

/**
 * 药品视图模型
 */
export interface MedicineView extends Medicine {
  medicineNamePinyin: string
  medicineNamePinyinAbbr: string
  registeredNamePinyin: string
  registeredNamePinyinAbbr: string
  createdAt: string
  updatedAt: string
  deletedAt: string
}

/**
 * 药品搜素模型
 */
export interface MedicineSearchModel {
  keywords: string
  medicineCode: string
  deleted: boolean
}

/**
 * 获取药品
 * @param id
 */
export function getMedicine(id: string) {
  return axios.get(`/medicines/${id}`)
}

/**
 * 搜索药品分页
 * @param data
 * @param page
 */
export function getMedicinePagination(data: MedicineSearchModel, page: Pager) {
  return axios.get('/medicines', { params: { ...data, ...page } })
}

/**
 * 添加药品
 * @param data
 */
export function createMedicine(data: Medicine) {
  return axios.post('/medicines', data)
}

/**
 * 修改药品信息
 * @param data
 */
export function updateMedicine(data: Medicine) {
  return axios.put(`/medicines/${data.id}`, data)
}

/**
 * 删除药品
 *
 * @param id
 */
export function deleteMedicine(id: string) {
  return axios.delete(`/medicines/${id}`)
}
