import axios from 'axios'
import { Pager } from '@/api/common'

// 中医证候

/**
 * 中医证候交互模型
 */
export interface Syndrome {
  id: string // ID
  syndromeCode: string // 证候编码
  syndromeName: string // 证候名称
  description: string // 证候描述
}

/**
 * 中医证候视图模型
 */
export interface SyndromeView extends Syndrome {
  createdAt: string // 创建时间
  updatedAt: string // 修改时间
}

/**
 * 中医证候搜素模型
 */
export interface SyndromeSearchModel {
  keywords: string // 关键词
  syndromeCode: string // 证候编码
  deleted: boolean // 是否删除
}

/**
 * 获取中医证候
 * @param id
 */
export function getSyndrome(id: string) {
  return axios.get(`/syndromes/${id}`)
}

/**
 * 搜索中医证候分页
 * @param data
 * @param page
 */
export function getSyndromePagination(data: SyndromeSearchModel, page: Pager) {
  return axios.get('/syndromes', { params: { ...data, ...page } })
}

/**
 * 添加中医证候
 * @param data
 */
export function createSyndrome(data: Syndrome) {
  return axios.post('/syndromes', data)
}

/**
 * 修改中医证候信息
 * @param data
 */
export function updateSyndrome(data: Syndrome) {
  return axios.put(`/syndromes/${data.id}`, data)
}

/**
 * 删除中医证候
 *
 * @param id
 */
export function deleteSyndrome(id: string) {
  return axios.delete(`/syndromes/${id}`)
}
