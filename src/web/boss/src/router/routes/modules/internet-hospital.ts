import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const InternetHospital: AppRouteRecordRaw = {
  path: '/institution',
  name: 'institution',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '互医',
    requiresAuth: false,
    icon: 'icon-bg-colors',
    order: 8000,
  },
  children: [
    {
      path: 'internet-hospital-setting',
      name: 'InternetHospitalSettingManage',
      component: () => import('@/views/internet-hospital/setting.vue'),
      meta: {
        locale: '互医配置',
        requiresAuth: false,
        buttons: ['internet-hospital-setting:modify'],
      },
    },
    {
      path: 'internet-hospital-departments',
      name: 'InternetHospitalDepartmentManage',
      component: () => import('@/views/internet-hospital/department.vue'),
      meta: {
        locale: '互医科室',
        requiresAuth: false,
        buttons: ['internet-hospital-department:add', 'internet-hospital-department:modify', 'internet-hospital-department:delete'],
      },
    },
    {
      path: 'hospitals',
      name: 'HospitalManageManage',
      component: () => import('@/views/internet-hospital/setting.vue'),
      meta: {
        locale: '关联医院',
        requiresAuth: false,
        buttons: ['hospital:add', 'hospital:modify', 'hospital:delete'],
      },
    },
    {
      path: 'kitchen',
      name: 'KitchenManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '配餐厨房',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate'],
      },
    },
    {
      path: 'laboratories',
      name: 'LaboratoryManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '检验实验室',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate'],
      },
    },
    {
      path: 'fleets',
      name: 'FleetManage',
      component: () => import('@/views/account/index.vue'),
      meta: {
        locale: '检验车队',
        requiresAuth: false,
        buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate'],
      },
    },
  ],
}

export default InternetHospital
