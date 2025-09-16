import axios from 'axios'
import { Pager } from '@/api/common'

export interface ChineseMedicinePiece {
  id: string
  pieceCode: string
  pieceName: string
  pieceAlias: string
  nature: string
  meridian: string
  indications: string
  dosage: string
}

export interface ChineseMedicinePieceView extends ChineseMedicinePiece {
  createdAt: string
  updatedAt: string
  deletedAt: string
}

export interface ChineseMedicinePieceSearchModel {
  keywords: string
  deleted: boolean
}

export interface ChineseMedicinePieceOption {
  id: string
  code: string
  name: string
  alias: string
}

export function getChineseMedicinePiece(id: string) {
  return axios.get(`/chinese-medicine-pieces/${id}`)
}

export function getChineseMedicinePieceOptions(data: string) {
  return axios.get<ChineseMedicinePieceOption>('/chinese-medicine-pieces/options', { params: { keywords: data } })
}

export function getChineseMedicinePiecePagination(data: ChineseMedicinePieceSearchModel, page: Pager) {
  return axios.get('/chinese-medicine-pieces', { params: { ...data, ...page } })
}

export function createChineseMedicinePiece(data: ChineseMedicinePiece) {
  return axios.post('/chinese-medicine-pieces', data)
}

export function updateChineseMedicinePiece(data: ChineseMedicinePiece) {
  return axios.put(`/chinese-medicine-pieces/${data.id}`, data)
}

export function deleteChineseMedicinePiece(id: string) {
  return axios.delete(`/chinese-medicine-pieces/${id}`)
}
