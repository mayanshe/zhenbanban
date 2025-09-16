import axios from 'axios'

// 快捷菜单 QuickMenu

/**
 * 快捷菜单
 */
export interface QuickMenu {
  pageName: string
  routeName: string
}

/**
 * 获取当前登陆用户的所有快捷菜单
 */
export function getQuickMenus() {
  return axios.get('/quick-menus')
}

/**
 * 为当前用户添加快捷菜单
 * @param data
 */
export function addQuickMenu(data: QuickMenu) {
  return axios.put('/quick-menus', data)
}
