import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const FORM: AppRouteRecordRaw = {
  path: '/form',
  name: 'form',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '表单',
    icon: 'icon-settings',
    requiresAuth: true,
    order: 24000,
  },
  children: [
    {
      path: 'step',
      name: 'Step',
      component: () => import('@/views/form/step/index.vue'),
      meta: {
        locale: '分步表单',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'group',
      name: 'Group',
      component: () => import('@/views/form/group/index.vue'),
      meta: {
        locale: '分组表单',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
}

export default FORM
