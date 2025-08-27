import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const User: AppRouteRecordRaw = {
  path: '/user',
  name: 'user',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '用户',
    requiresAuth: false,
    icon: 'icon-user-group',
    order: 8500,
  },
  children: [
    {
      path: 'account',
      name: 'AccountManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '账号',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'doctors',
      name: 'DoctorManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '医生',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'nurses',
      name: 'NurseManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '医师',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'pharmacist',
      name: 'PharmacistManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '药师',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'patient-escorts',
      name: 'PatientEscortManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '伴诊员',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      },
    },
    {
      path: 'technicians',
      name: 'TechnicianManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '检验师',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      }
    },
    {
      path: 'drivers',
      name: 'DriverManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '司机',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
      }
    },
  ],
}

export default User
