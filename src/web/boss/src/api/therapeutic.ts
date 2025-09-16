import axios from 'axios'
import { Pager } from '@/api/common'

// 中医治法 Therapeutic

/**
 * 治法模型
 */
export interface Therapeutic {
  id: string // 治法ID
  therapeuticsCode: string // 治法编码
  therapeuticsName: string // 治法名称
  description: string // 治法描述
}

/**
 * 治法视图模型
 */
export interface TherapeuticView extends Therapeutic {
  therapeuticsNamePinyin: string // 治法名称拼音
  therapeuticsNamePinyinAbbr: string // 治法名称拼音首字母缩写
  createdAt: string // 创建时间
  updatedAt: string // 修改时间
}

/**
 * 治法搜索模型
 */
export interface TherapeuticSearchModel {
  keywords: string // 关键词
  therapeuticsCode: string // 治法编码
  deleted: boolean // 是否删除
}

/**
 * 获取治法
 * @param id
 */
export function getTherapeutic(id: string) {
  return axios.get(`/therapeutics/${id}`)
}

/**
 * 搜索治法分页
 * @param data
 * @param page
 */
export function getTherapeuticPagination(data: TherapeuticSearchModel, page: Pager) {
  return axios.get('/therapeutics', { params: { ...data, ...page } })
}

/**
 * 添加治法
 * @param data
 */
export function createTherapeutic(data: Therapeutic) {
  return axios.post('/therapeutics', data)
}

/**
 * 修改治法信息
 * @param data
 */
export function updateTherapeutic(data: Therapeutic) {
  return axios.put(`/therapeutics/${data.id}`, data)
}

/**
 * 删除治法
 *
 * @param id
 */
export function deleteTherapeutic(id: string) {
  return axios.delete(`/therapeutics/${id}`)
}
