import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const PROFILE: AppRouteRecordRaw = {
  path: '/profile',
  name: 'profile',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '个人资料',
    requiresAuth: true,
    icon: 'icon-file',
    order: 24000,
  },
  children: [
    {
      path: 'basic',
      name: 'Basic',
      component: () => import('@/views/profile/basic/index.vue'),
      meta: {
        locale: '个人基础资料',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
}

export default PROFILE
