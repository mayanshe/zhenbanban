import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const Dictionary: AppRouteRecordRaw = {
  path: '/dictionary',
  name: 'dictionary',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '字典',
    requiresAuth: false,
    icon: 'icon-book',
    order: 9000,
  },
  children: [
    {
      path: 'diagnoses',
      name: 'DiagnoseManage',
      component: () => import('@/views/diagnose/index.vue'),
      meta: {
        locale: '疾病诊断',
        requiresAuth: false,
        buttons: ['diagnose:add', 'diagnose:modify', 'diagnose:delete', 'diagnose:activate'],
      },
    },
    {
      path: 'chinese-medicine-pieces',
      name: 'ChineseMedicinePieces',
      component: () => import('@/views/chinese-medicine-piece/index.vue'),
      meta: {
        locale: '中药饮片',
        requiresAuth: false,
        buttons: [
          'chinese-medicine-piece:add',
          'chinese-medicine-piece:modify',
          'chinese-medicine-piece:delete',
          'chinese-medicine-piece:activate',
        ],
      },
    },
    {
      path: 'medicines',
      name: 'MedicineManage',
      component: () => import('@/views/medicine/index.vue'),
      meta: {
        locale: '西药/中成药',
        requiresAuth: false,
        buttons: ['medicine:add', 'medicine:modify', 'medicine:delete', 'medicine:activate'],
      },
    },
    {
      path: 'syndromes',
      name: 'SyndromeManage',
      component: () => import('@/views/syndrome/index.vue'),
      meta: {
        locale: '中医证侯',
        requiresAuth: false,
        buttons: ['syndrome:add', 'syndrome:modify', 'syndrome:delete'],
      },
    },
    {
      path: 'therapeutics',
      name: 'TherapeuticManage',
      component: () => import('@/views/therapeutic/index.vue'),
      meta: {
        locale: '中医治法',
        requiresAuth: false,
        buttons: ['therapeutic:add', 'therapeutic:modify', 'therapeutic:delete'],
      },
    },
    {
      path: 'antagonisms',
      name: 'AntagonismManage',
      component: () => import('@/views/antagonism/index.vue'),
      meta: {
        locale: '十八反十九畏',
        requiresAuth: false,
        buttons: ['antagonism:add', 'antagonism:modify', 'antagonism:delete'],
      },
    },
  ],
}

export default Dictionary
