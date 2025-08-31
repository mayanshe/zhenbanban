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
        buttons: ['diagnose:add', 'diagnose:modify', 'diagnose:delete', 'diagnose:activate']
      },
    },
    {
      path: 'chinese-medicine-pieces',
      name: 'ChineseMedicinePieces',
      component: () => import('@/views/chinese-medicine-piece/index.vue'),
      meta: {
        locale: '中药饮片',
        requiresAuth: false,
        buttons: ['chinese-medicine-piece:add', 'chinese-medicine-piece:modify', 'chinese-medicine-piece:delete', 'chinese-medicine-piece:activate']
      },
    },
    {
      path: 'medicines',
      name: 'MedicineManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '西药及中成药',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'syndromes',
      name: 'SyndromeManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '中医证侯',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'therapeutics',
      name: 'TherapeuticManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '中医治法',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'contraindications',
      name: 'ContraindicationManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '中医禁忌',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    }
  ],
}

export default Dictionary
