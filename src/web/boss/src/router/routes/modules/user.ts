import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const USER: AppRouteRecordRaw = {
  path: '/user',
  name: 'user',
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
        roles: ['*'],
      },
    },
    {
      path: 'setting',
      name: 'Setting',
      component: () => import('@/views/user/setting/index.vue'),
      meta: {
        locale: '用户配置',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
}

export default USER
