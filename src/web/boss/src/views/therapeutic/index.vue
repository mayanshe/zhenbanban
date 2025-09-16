<template>
  <div class="container">
    <Breadcrumb :items="['字典', '治法管理']" page-name="中医治法" route-name="中医治法" />
    <a-card class="general-card" title="治法管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="治法编码或名称" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="therapeuticsCode" label="治法编码:">
                  <a-input v-model="searchData.therapeuticsCode" placeholder="精确查找" />
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
            <a-button v-if="buttons.includes('therapeutic:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加中医治法
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
        :data="therapeutics.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button v-if="buttons.includes('therapeutic:modify')" type="text" size="mini" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此治法?" @ok="handleDelete(record.id)" v-if="buttons.includes('therapeutic:delete')">
              <a-button type="text" size="mini" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 治法编辑窗 -->
    <TherapeuticSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { TherapeuticView, TherapeuticSearchModel, getTherapeuticPagination, deleteTherapeutic } from '@/api/therapeutic'
import TherapeuticSingle from './components/single.vue'

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

// region 治法列表
// 表格列定义
const columns = [
  {
    title: '治法编码',
    dataIndex: 'therapeuticsCode',
  },
  {
    title: '治法名称',
    dataIndex: 'therapeuticsName',
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

const pager = ref<Pager>({
  page: 1,
  pageSize: 15,
})

const generateSearchModel = () => {
  return {
    keywords: '',
    therapeuticsCode: '',
    deleted: false,
  }
}

const reset = () => {
  searchData.value = generateSearchModel()
  search()
}

const generateDatalist = () => {
  pager.value = {
    page: 1,
    pageSize: 15,
  }

  return {
    page: 1,
    pageSize: 15,
    count: 0,
    total: 0,
    totalPage: 0,
    prevPage: 1,
    nextPage: 1,
    items: [] as TherapeuticView[],
  }
}

const searchData = ref<TherapeuticSearchModel>(generateSearchModel())
const therapeutics = ref<Pagination<TherapeuticView>>(generateDatalist())

const load = async () => {
  therapeutics.value = (await getTherapeuticPagination(searchData.value, pager.value)) || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: therapeutics.value.total,
    pageSize: therapeutics.value.pageSize,
    current: therapeutics.value.page,
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
  deleteTherapeutic(id).then(() => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'TherapeuticManage',
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
</style>
