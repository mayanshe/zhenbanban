import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const Users: AppRouteRecordRaw = {
  path: '/users',
  name: 'users',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '用户',
    icon: 'icon-user',
    requiresAuth: true,
    order: 26000,
  },
  children: [
    {
      path: 'info',
      name: 'Info',
      component: () => import('@/views/user/info/index.vue'),
      meta: {
        locale: '用户信息',
        requiresAuth: true,
        roles: ['a'],
      },
    },
    {
      path: 'setting',
      name: 'Setting',
      component: () => import('@/views/user/setting/index.vue'),
      meta: {
        locale: '用户配置',
        requiresAuth: true,
        roles: ['a'],
      },
    },
  ],
}

export default Users
