<template>
  <div class="container">
    <Breadcrumb :items="['系统', '角色管理']" />
    <a-card class="general-card" title="角色管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="权限名或权限显示名" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 28px" direction="vertical" />
        <a-col :flex="'200px'" style="text-align: right">
          <a-space :size="18">
            <a-button @click="search" type="primary" size="small">
              <template #icon>
                <icon-search />
              </template>
              搜索
            </a-button>
            <a-button @click="reset" size="small">
              <template #icon>
                <icon-refresh />
              </template>
              重置
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <a-divider style="margin-top: 0" />

      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button v-if="buttons.includes('role:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加角色
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="load"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>

      <a-table style="margin-bottom: 16px" :columns="columns" :data="roles.items" :pagination="pagination" @page-change="handlePageChange">
        <template #optional="{ record }">
          <a-space>
            <a-button
              @click="handleOpenAssignment(record.id)"
              v-if="buttons.includes('role:assignment') && record.roleName !== 'administrator'"
              type="secondary"
              size="mimi"
            >
              配置资源与权限
            </a-button>
            <a-button
              v-if="buttons.includes('role:modify') && record.roleName !== 'administrator'"
              type="text"
              size="mimi"
              @click="handleOpenSingle('modify', record.id)"
            >
              编辑
            </a-button>
            <a-popconfirm
              content="确定删除此角色?"
              @ok="handleDelete(record.id)"
              v-if="buttons.includes('role:delete') && record.roleName !== 'administrator'"
            >
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 角色编辑窗 -->
    <Single v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />

    <!-- 分配编辑窗 -->
    <Assignment v-model:open="assignment.open" :role-id="assignment.id" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, unref } from 'vue'
import { useRoute } from 'vue-router'
import { Message, Pagination } from '@arco-design/web-vue'
import { Pager } from '@/api/common'
import { RoleSearchModel, RoleView, getRolePagination, deleteRole } from '@/api/role'
import Single from './components/single.vue'
import Assignment from './components/assignment.vue'

const route = useRoute()
const buttons = route.meta.buttons || []

// region 添加及编辑的交互
const single = ref({
  open: false,
  action: '',
  id: '0',
})

// region 配置菜单及权限
const assignment = ref({
  open: false,
  id: '0',
})

const handleOpenAssignment = async (idValue: string) => {
  assignment.value = {
    open: true,
    id: idValue,
  }
}
// endregion

// 打开编辑框
const handleOpenSingle = async (actionValue: string, idValue: string) => {
  single.value = {
    open: true,
    action: actionValue,
    id: idValue,
  }
}

// 响应弹窗成功事件
const handleSuccess = () => {
  search()
}
// endregion

// region 搜索
const generateSearchData = () => {
  return {
    keywords: '',
  }
}

const searchData = ref<RoleSearchModel>(generateSearchData())

const pager = ref<Pager>({
  page: 1,
  pageSize: 10,
})

const generateRoles = async () => {
  return {
    page: 1,
    pageSize: 10,
    count: 0,
    total: 0,
    totalPage: 0,
    prevPage: 1,
    nextPage: 1,
    items: [] as RoleView[],
  }
}

const roles = ref<Pagination>(generateRoles())

const load = async () => {
  roles.value = (await getRolePagination(unref(searchData), unref(pager))) || generateRoles()
}

const search = async () => {
  pager.value.page = 1
  load()
}

const reset = () => {
  searchData.value = generateSearchData()
  search()
}

load()
// endregion

// 表格
const columns = [
  {
    title: '显示名称',
    dataIndex: 'displayName',
  },
  {
    title: '角色名称',
    dataIndex: 'roleName',
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
  },
  {
    title: '更新时间',
    dataIndex: 'updatedAt',
  },
  {
    title: '操作',
    slotName: 'optional',
  },
]

const pagination = computed(() => {
  return {
    total: roles.value.total,
    pageSize: roles.value.pageSize,
    current: roles.value.page,
    totalPage: roles.value.totalPage,
    showPageSizeChanger: true,
    showTotal: true,
  }
})

const handlePageChange = (page: number) => {
  pager.value.page = page
  load()
}
// endregion

const handleDelete = async (id: string) => {
  deleteRole(id).then(() => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'RoleManage',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}

.action-icon {
  margin-left: 12px;
  cursor: pointer;
}

.active {
  color: #0960bd;
  background-color: #e3f4fc;
}

.setting {
  display: flex;
  align-items: center;
  width: 200px;

  .title {
    margin-left: 12px;
    cursor: pointer;
  }
}
</style>
