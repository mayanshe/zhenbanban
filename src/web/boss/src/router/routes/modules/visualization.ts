import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const VISUALIZATION: AppRouteRecordRaw = {
  path: '/visualization',
  name: 'visualization',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '数据可视化',
    requiresAuth: true,
    icon: 'icon-apps',
    order: 20000,
  },
  children: [
    {
      path: 'data-analysis',
      name: 'DataAnalysis',
      component: () => import('@/views/visualization/data-analysis/index.vue'),
      meta: {
        locale: '分析页',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'multi-dimension-data-analysis',
      name: 'MultiDimensionDataAnalysis',
      component: () => import('@/views/visualization/multi-dimension-data-analysis/index.vue'),
      meta: {
        locale: '多维数据分析',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
}

export default VISUALIZATION
