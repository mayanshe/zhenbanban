import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const MealPreparation: AppRouteRecordRaw = {
    path: '/meal-preparation',
    name: 'MealPreparation',
    component: DEFAULT_LAYOUT,
    meta: {
        locale: '配餐',
        requiresAuth: false,
        icon: 'icon-sun-fill',
        order: 7500,
    },
    children: [
        {
            path: 'meal-orders',
            name: 'MealOrderManage',
            component: () => import('@/views/account/index.vue'),
            meta: {
                locale: '订单',
                requiresAuth: false,
                buttons: ['account:add', 'account:modify', 'account:delete', 'account:activate']
            },
        },
    ]
}

export default MealPreparation