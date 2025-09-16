<template>
  <div class="container">
    <Breadcrumb :items="['字典', '十八反十九畏']" page-name="十八反十九畏" route-name="AntagonismManage" />
    <a-card class="general-card" title="十八反十九畏管理">
      <a-divider style="margin-top: 0" />
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="饮片编码或名称" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="type" label="类型:">
                  <a-select v-model="searchData.kind" placeholder="请选择类型" allow-clear>
                    <a-option :value="18">十八反</a-option>
                    <a-option :value="19">十九畏</a-option>
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
              添加记录
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="search"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>
      <a-table :columns="columns" :data="antagonisms.items" :pagination="pagination" @page-change="handlePageChange">
        <template #pieceName="{ record }">{{ record.pieceName }}({{ record.pieceCode }}, {{ record.pieceAlias }})</template>
        <template #conflictPieceName="{ record }">
          {{ record.conflictPieceName }}({{ record.conflictPieceCode }}, {{ record.conflictPieceAlias }})
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mini" @click="handleOpenSingle('modify', record.id)">编辑</a-button>
            <a-popconfirm content="确定删除此记录?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mini" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <AntagonismSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { AntagonismView, AntagonismSearchModel, getAntagonismPagination, deleteAntagonism } from '@/api/antagonism'
import AntagonismSingle from './components/single.vue'

const single = ref({
  open: false,
  action: 'add',
  id: '0',
})

const handleOpenSingle = (action: string, id: string) => {
  single.value = {
    open: true,
    action,
    id,
  }
}

const handleSuccess = () => {
  load()
}

const columns = [
  { title: '类型', dataIndex: 'kindName' },
  { title: '中药名称', slotName: 'pieceName' },
  { title: '禁忌配伍名称', slotName: 'conflictPieceName' },
  { title: '操作', slotName: 'optional' },
]

const pager = ref<Pager>({
  page: 1,
  pageSize: 15,
})

const generateSearchModel = (): AntagonismSearchModel => ({
  keywords: '',
  kind: undefined,
})

const searchData = ref<AntagonismSearchModel>(generateSearchModel())

const reset = () => {
  searchData.value = generateSearchModel()
  search()
}

const generateDataList = (): Pagination<AntagonismView> => ({
  page: 1,
  pageSize: 15,
  count: 0,
  total: 0,
  totalPage: 0,
  prevPage: 1,
  nextPage: 1,
  items: [],
})

const antagonisms = ref<Pagination<AntagonismView>>(generateDataList())

const load = async () => {
  const res = await getAntagonismPagination(searchData.value, pager.value)
  antagonisms.value = res || generateDataList()
}

const search = () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => ({
  total: antagonisms.value.total,
  pageSize: antagonisms.value.pageSize,
  current: antagonisms.value.page,
}))

const handlePageChange = (page: number) => {
  pager.value.page = page
  load()
}

const handleDelete = async (id: string) => {
  try {
    await deleteAntagonism(id)
    Message.success('删除成功')
    search()
  } catch (error) {
    // Error handling
  }
}
</script>

<script lang="ts">
export default {
  name: 'AntagonismManage',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
