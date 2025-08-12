import axios from 'axios'
import { AdminState } from '@/store/modules/admin/types'
import type { RouteRecordNormalized } from 'vue-router'

/**
 * 管理员登陆请求数据
 */
export interface Credential {
  account: string // 账号
  password: string // 密码
  rememberMe: boolean // 记住我
}

/**
 * 管理员登陆请求返回的登陆凭证
 */
export interface AccessToken {
  type: string // 验证类型
  token: string // 载荷
  expireIn: number // 过期时间（毫秒)
  timestamp: number // 创建时间
}

/**
 * 管理员登陆请求
 * @param data
 */
export function login(data: Credential) {
  return axios.post<AccessToken>('/access-tokens', data)
}

/**
 * 管理员退出登陆
 */
export function logout() {
  return axios.delete('/access-tokens')
}

/**
 * 获取管理员信息
 */
export function getAdminState() {
  return axios.get<AdminState>('/my/profile')
}

/**
 * 获取管理员菜单列表
 * @param adminId
 */
export function getMenuList(adminId: number) {
  return axios.get<RouteRecordNormalized[]>(`/admins/${adminId}/menus`)
}
