<template>
  <div class="container">
    <Breadcrumb :items="['系统', '权限组管理']" />
    <a-card class="general-card" title="权限组管理">
      <a-divider style="margin-top: 0" />
      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button v-if="buttons.includes('permission-group:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加权限组
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="load"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>
      <a-table style="margin-bottom: 16px" :columns="columns" :data="datalist" :bordered="false" :pagination="false" :loading="loading">
        <template #optional="{ record }">
          <a-space>
            <a-button
              v-if="buttons.includes('permission-group:modify')"
              type="text"
              size="mimi"
              @click="handleOpenSingle('modify', record.id)"
            >
              编辑
            </a-button>
            <a-popconfirm
              content="确定删除此权限组?"
              @ok="handleDelete(record.id)"
              v-if="buttons.includes('permission-group:delete') && record.children.length === 0"
            >
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 权限组编辑窗 -->
    <PermissionGroupSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { useRoute } from 'vue-router'
import { PermissionGroupView, getPermissionGroupList, deletePermissionGroup } from '@/api/permission-group'
import PermissionGroupSingle from './components/single.vue'

const route = useRoute()
const buttons = route.meta.buttons || []

// region 添加及编辑的交互
const single = ref({
  open: false,
  action: '',
  id: '0',
})

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
  load()
}
// endregion

// region 权限组列表
const loading = ref(false)

// 列表数据
const datalist = ref<PermissionGroupView[]>([])

// 加载列表
const load = async () => {
  loading.value = true
  datalist.value = (await getPermissionGroupList()) || []
  loading.value = false
}

// 加载页面时加载
load()

// 表格定义
const columns = [
  {
    title: '显示名称',
    dataIndex: 'displayName',
  },
  {
    title: '权限组名',
    dataIndex: 'groupName',
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
  },
  {
    title: '修改时间',
    dataIndex: 'updatedAt',
  },
  {
    title: '操作',
    slotName: 'optional',
  },
]

// 删除权限组处理
const handleDelete = async (id: string) => {
  deletePermissionGroup(id).then(() => {
    Message.success('删除成功')
    load()
  })
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'PermissionGroupManage',
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
