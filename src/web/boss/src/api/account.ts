import axios from 'axios'
import { Pager, Pagination, ValueObject } from '@/api/common'

// 账号（Account）

/**
 * 账号
 */
export interface Account {
    id: string // 账号id
    scopes: string // 账号作用域
    username: string // 用户名
    surname: string // 姓
    givenName: string // 名
    gender: string // 性别
    email: string // 邮箱
    phone: string // 手机号
    avatar: string // 头像
    state: number // 状态
    password?: string // 密码
}

/**
 * 账号视图
 */
export interface AccountView {
    id: string // 账号id
    scopes: ValueObject[] // 账号作用域
    username: string // 用户名
    surname: string // 姓
    givenName: string // 名
    gender: ValueObject // 性别
    email: string // 邮箱
    phone: string // 手机号
    avatar: string // 头像
    state: ValueObject // 状态
    createdAt: string // 创建时间
    updatedAt: string // 修改时间
}

/**
 * 账号搜索模型
 */
export interface AccountSearchModel {
    id: string // 账号id
    username: string // 用户ing
    phone: string // 手机号
    scope: string // 账号作用域
    state: number // 状态
}

/**
 * 创建账号
 * @param data
 */
export function createAccount(data: Account) {
    return axios.post('/accounts', data)
}

/**
 * 修改账号信息
 * @param data
 */
export function modifyAccount(data: Account) {
    return axios.put(`/accounts/${data.id}`, data)
}

/**
 * 禁用账号
 * @param id
 */
export function deleteAccount(id: string) {
    return axios.delete(`/accounts/${id}/lock`)
}

/**
 * 激活账号
 * @param id
 */
export function activateAccount(id: string) {
    return axios.delete(`/accounts/${id}/lock`)
}

/**
 * 获取账号信息
 * @param id
 */
export function getAccount(id: number) {
    return axios.get(`/accounts/${id}`)
}

/**
 * 获取账号分页
 * @param data
 * @param pager
 */
export function getAccountPagination(data: AccountSearchModel, pager: Pager) {
    return axios.get('/accounts', { params: { ...data, ...pager } })
}