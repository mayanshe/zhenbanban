<template>
  <div class="container">
    <Breadcrumb :items="['数据字典', '中药饮片']" />
    <a-card class="general-card" title="中药饮片管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="饮片编码、名称、别名等" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="deleted" label="状态:">
                  <a-select v-model="searchData.deleted" placeholder="请选择">
                    <a-option :value="false">正常</a-option>
                    <a-option :value="true">已删除</a-option>
                  </a-select>
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
            <a-button type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加
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
        :data="pieces.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此条目?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <Single v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { ChineseMedicinePieceView, ChineseMedicinePieceSearchModel, getChineseMedicinePiecePagination, deleteChineseMedicinePiece } from '@/api/chinese-medicine-piece'
import Single from './components/single.vue'

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

const columns = [
  { title: '编码', dataIndex: 'pieceCode' },
  { title: '名称', dataIndex: 'pieceName' },
  { title: '别名', dataIndex: 'pieceAlias' },
  { title: '性味', dataIndex: 'nature' },
  { title: '归经', dataIndex: 'meridian' },
  { title: '创建时间', dataIndex: 'createdAt' },
  { title: '操作', slotName: 'optional' },
]

const pager = ref<Pager>({
  page: 1,
  pageSize: 10,
})

const generateSearchModel = () => {
  return {
    keywords: '',
    deleted: false,
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
    items: [] as ChineseMedicinePieceView[],
  }
}

const searchData = ref<ChineseMedicinePieceSearchModel>(generateSearchModel())
const pieces = ref<Pagination<ChineseMedicinePieceView>>(generateDatalist())

const load = async () => {
  const result = await getChineseMedicinePiecePagination(searchData.value, pager.value)
  pieces.value = result.data || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: pieces.value.total,
    pageSize: pieces.value.pageSize,
    current: pieces.value.page,
    showPageSizeChanger: true,
    showTotal: true,
  }
})

const handlePageChange = (page: number) => {
  pager.value.page = page
  load()
}

const handleDelete = async (id: string) => {
  deleteChineseMedicinePiece(id).then(() => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'ChineseMedicinePiece',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
