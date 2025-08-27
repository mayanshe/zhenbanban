import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const EXCEPTION: AppRouteRecordRaw = {
  path: '/exception',
  name: 'exception',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '错误页面',
    requiresAuth: true,
    icon: 'icon-exclamation-circle',
    order: 21000,
  },
  children: [
    {
      path: '403',
      name: '403',
      component: () => import('@/views/exception/403/index.vue'),
      meta: {
        locale: '403',
        requiresAuth: true,
        roles: ['a'],
      },
    },
    {
      path: '404',
      name: '404',
      component: () => import('@/views/exception/404/index.vue'),
      meta: {
        locale: '404',
        requiresAuth: true,
        roles: ['a'],
      },
    },
    {
      path: '500',
      name: '500',
      component: () => import('@/views/exception/500/index.vue'),
      meta: {
        locale: '500',
        requiresAuth: true,
        roles: ['a'],
      },
    },
  ],
}

export default EXCEPTION
