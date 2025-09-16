import axios from 'axios'
import { Pager } from '@/api/common'

/**
 * 十八反十九畏交互模型
 */
export interface Antagonism {
  id: string
  kind: number
  pieceId: string
  conflictPieceId: string
}

/**
 * 十八反十九畏视图模型
 */
export interface AntagonismView extends Antagonism {
  kindName: string
  pieceCode: string
  pieceName: string
  pieceAlias: string
  conflictPieceCode: string
  conflictPieceName: string
  conflictPieceAlias: string
}

/**
 * 十八反十九畏搜索模型
 */
export interface AntagonismSearchModel {
  keywords?: string
  kind?: number
}

/**
 * 获取十八反十九畏
 * @param id
 */
export function getAntagonism(id: string) {
  return axios.get(`/antagonisms/${id}`)
}

/**
 * 搜索十八反十九畏分页
 * @param data
 * @param page
 */
export function getAntagonismPagination(data: AntagonismSearchModel, page: Pager) {
  return axios.get('/antagonisms', { params: { ...data, ...page } })
}

/**
 * 添加十八反十九畏
 * @param data
 */
export function createAntagonism(data: Antagonism) {
  return axios.post('/antagonisms', data)
}

/**
 * 修改十八反十九畏信息
 * @param data
 */
export function updateAntagonism(data: Antagonism) {
  return axios.put(`/antagonisms/${data.id}`, data)
}

/**
 * 删除十八反十九畏
 * @param id
 */
export function deleteAntagonism(id: string) {
  return axios.delete(`/antagonisms/${id}`)
}
