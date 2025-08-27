import { DEFAULT_LAYOUT } from '../base'
import { AppRouteRecordRaw } from '../types'

const LIST: AppRouteRecordRaw = {
  path: '/system',
  name: 'system',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '系统',
    requiresAuth: false,
    icon: 'icon-settings',
    order: 9500,
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
      component: () => import('@/views/role/index.vue'),
      meta: {
        locale: '角色',
        requiresAuth: false,
        buttons: ['role:add', 'role:modify', 'role:delete', 'role:assignment'],
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
    {
      path: 'medias',
      name: 'MediaManage',
      component: () => import('@/views/media/index.vue'),
      meta: {
        locale: '媒体',
        requiresAuth: false,
        buttons: ['media:add', 'media:modify', 'media:delete'],
      },
    },
    {
      path: 'options',
      name: 'OptionManage',
      component: () => import('@/views/option/index.vue'),
      meta: {
        locale: '配置',
        requiresAuth: false,
        buttons: ['option:add', 'option:modify', 'option:delete'],
      },
    },
    {
      path: 'sms',
      name: 'SmsManage',
      component: () => import('@/views/sms/index.vue'),
      meta: {
        locale: '短信',
        requiresAuth: false,
        buttons: ['sms:send', 'sms:resend'],
      },
    },
    {
      path: 'notifications',
      name: 'NotificationManage',
      component: () => import('@/views/notification/index.vue'),
      meta: {
        locale: '推送',
        requiresAuth: false,
        buttons: ['sms:send', 'sms:resend'],
      },
    },
    {
      path: 'login-logs',
      name: 'LoginLogManage',
      component: () => import('@/views/login-log/index.vue'),
      meta: {
        locale: '登陆日志',
        requiresAuth: false,
        buttons: ['sms:send', 'sms:resend'],
      },
    },
    {
      path: 'operate-logs',
      name: 'OperateLogManage',
      component: () => import('@/views/operate-log/index.vue'),
      meta: {
        locale: '操作日志',
        requiresAuth: false,
        buttons: ['sms:send', 'sms:resend'],
      },
    },
  ],
}

export default LIST
