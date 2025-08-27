import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const PreConsultation: AppRouteRecordRaw = {
    path: '/pre-consultation',
    name: 'PreConsultation',
    component: DEFAULT_LAYOUT,
    meta: {
        locale: '诊前',
        requiresAuth: false,
        icon: 'icon-list',
        order: 1500,
    },
    children: [
        {
            path: 'consults',
            name: 'ConsultManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '咨询',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
        {
            path: 'schedules',
            name: 'ScheduleManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '排班',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
        {
            path: 'registration',
            name: 'RegistrationManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '挂号',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
    ]
}

export default PreConsultation