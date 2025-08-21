<template>
  <div class="container">
    <Breadcrumb :items="['系统', '资源管理']" />
    <a-card class="general-card" title="资源管理（菜单 / 链接 / 按钮）">
      <a-divider style="margin-top: 0" />

      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button v-if="buttons.includes('resource:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加资源
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="loadResources"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>

      <a-table style="margin-bottom: 16px" :columns="columns" :data="resources" :pagination="false">
        <template #optional="{ record }">
          <a-space>
            <a-button v-if="buttons.includes('resource:modify')" type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm
              content="确定删除此资源?"
              @ok="handleDelete(record.id)"
              v-if="buttons.includes('resource:delete') && record.children.length === 0"
            >
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 权限组编辑窗 -->
    <ResourceSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { ResourceView, getResourceList, deleteResource } from '@/api/resource'
import ResourceSingle from './components/single.vue'

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
  loadResources()
}
// endregion

// region 列表加载
// 表格定义
const columns = [
  {
    title: '显示名称',
    dataIndex: 'displayName',
  },
  {
    title: '资源名称',
    dataIndex: 'resourceName',
  },
  {
    title: '资源类型',
    dataIndex: 'resourceType.name',
  },
  {
    title: '路径/Url',
    dataIndex: 'path',
    render: ({ record }) => {
      return record.resourceType.code.toLowerCase() === 'link' ? record.url : record.path
    },
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

const resources = ref<ResourceView[]>([])

const loadResources = async () => {
  resources.value = (await getResourceList()) || []
}

loadResources()

const handleDelete = async (id: string) => {
  deleteResource(id).then(() => {
    Message.success('删除成功')
    loadResources()
  })
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'ResourceManage',
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
