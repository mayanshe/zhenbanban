import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const LIST: AppRouteRecordRaw = {
  path: '/system',
  name: 'system',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '系统',
    requiresAuth: true,
    icon: 'icon-apps',
    order: 9900,
  },
  children: [
    {
      path: 'admins', // The midline path complies with SEO specifications
      name: 'AdminManage',
      component: () => import('@/views/list/search-table/index.vue'),
      meta: {
        locale: '管理员',
        requiresAuth: false,
      },
    },
    {
      path: 'roles',
      name: 'RoleManage',
      component: () => import('@/views/list/card/index.vue'),
      meta: {
        locale: '角色',
        requiresAuth: false,
      },
    },
    {
      path: 'resources',
      name: 'ResourceManage',
      component: () => import('@/views/resource/index.vue'),
      meta: {
        locale: '资源',
        requiresAuth: false,
        buttons: ['resource:add', 'resource:modify', 'resource:delete'],
      },
    },
    {
      path: 'permissions',
      name: 'PermissionManage',
      component: () => import('@/views/permission/index.vue'),
      meta: {
        locale: '权限',
        requiresAuth: false,
        buttons: ['permission:add', 'permission:modify', 'permission:delete'],
      },
    },
    {
      path: 'permission-groups',
      name: 'PermissionGroupManage',
      component: () => import('@/views/permission-group/index.vue'),
      meta: {
        locale: '权限组',
        requiresAuth: false,
        buttons: ['permission-group:add', 'permission-group:modify', 'permission-group:delete'],
      },
    },
  ],
}

export default LIST
