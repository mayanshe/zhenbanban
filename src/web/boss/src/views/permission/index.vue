<template>
  <div class="container">
    <Breadcrumb :items="['系统', '权限管理']" />
    <a-card class="general-card" title="权限管理">
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
              <a-col :span="8">
                <a-form-item field="groupId" label="权限组:">
                  <a-cascader v-model="searchData.groupId" :options="groupOptions" placeholder="请选择" />
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
            <a-button v-if="buttons.includes('permission:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加权限
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="search"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>

      <a-table
        style="margin-bottom: 16px"
        :columns="columns"
        :data="permissions.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button
              v-if="buttons.includes('permission:modify')"
              type="text"
              size="mimi"
              @click="handleOpenSingle('modify', record.id)"
            >
              编辑
            </a-button>
            <a-popconfirm content="确定删除此权限?" @ok="handleDelete(record.id)"  v-if="buttons.includes('permission:delete')">
              <a-button
                type="text"
                size="mimi"
                status="danger"
              >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 权限组编辑窗 -->
    <PermissionSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { CascaderOption, Pager, Pagination } from '@/api/common'
import { PermissionGroupView, getPermissionGroupList } from '@/api/permission-group'
import { PermissionView, PermissionSearchModel, getPermissionPagination, deletePermission } from '@/api/permission'
import PermissionSingle from './components/single.vue'

const route = useRoute()
const buttons = route.meta.buttons || []

// region 添加与编辑的交互
const single = ref({
  open: false,
  action: '',
  id: '0',
})

const handleOpenSingle = async (actionValue: string, idValue: string) => {
  single.value = {
    open: true,
    action: actionValue,
    id: idValue,
  }
}

const handleSuccess = async () => {
  load()
}
// endregion

// region 加载权限组
const groupOptions = ref<CascaderOption[]>([])

const loadPermissionGroupDataList = async () => {
  const groups = (await getPermissionGroupList()) || []
  if (groups.length > 0) {
    const options = []
    for (let i = 0; i < groups.length; i += 1) {
      const item: PermissionGroupView = groups[i]
      const cs = []
      if (item.children.length > 0) {
        for (let m = 0; m < item.children.length; m += 1) {
          const child = item.children[m]
          cs.push({
            value: child.id,
            label: child.displayName,
          })
        }
      }
      options.push({
        value: item.id,
        label: item.displayName,
        children: cs,
      })
    }
    groupOptions.value = options
  }
}

loadPermissionGroupDataList()
// endregion

// region 权限列表
// 表格列定义
const columns = [
  {
    title: '显示名称',
    dataIndex: 'displayName',
  },
  {
    title: '权限名称',
    dataIndex: 'permissionName',
  },
  {
    title: '权限组',
    dataIndex: 'groupName',
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
    slotName: 'optional'
  }
]

const pager = ref<Pager>({
  page: 1,
  pageSize: 10,
})

const generateSearchModel = () => {
  return {
    keywords: '',
    groupId: '',
  }
}

const reset = () => {
  searchData.value = generateSearchModel()
}

const generateDatalist = () => {
  pager.value = {
    page: 1,
    pageSize: 10,
  }

  return {
    page: 1,
    pageSize: 10,
    count: 0,
    total: 0,
    totalPage: 0,
    prevPage: 1,
    nextPage: 1,
    items: [] as PermissionView[],
  }
}

const searchData = ref<PermissionSearchModel>(generateSearchModel())
const permissions = ref<Pagination<PermissionView>>(generateDatalist())

const load = async () => {
  permissions.value = (await getPermissionPagination(searchData.value, pager.value)) || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: permissions.value.total,
    pageSize: permissions.value.pageSize,
    current: permissions.value.page,
    totalPage: permissions.value.totalPage,
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
  deletePermission(id).then((res) => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'PermissionManage',
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
