import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const Institution: AppRouteRecordRaw = {
    path: '/institution',
    name: 'institution',
    component: DEFAULT_LAYOUT,
    meta: {
        locale: '资产',
        requiresAuth: false,
        icon: 'icon-bug',
        order: 8000,
    },
    children: [
        {
            path: 'hospitals',
            name: 'HospitalManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '医院',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
        {
            path: 'laboratories',
            name: 'LaboratoryManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '实验室',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
        {
            path: 'fleets',
            name: 'FleetManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '车队',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
        {
            path: 'kitchen',
            name: 'KitchenManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '厨房',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
    ]
}

export default Institution