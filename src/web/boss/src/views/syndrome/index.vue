<template>
  <div class="container">
    <Breadcrumb :items="['字典', '中医证候']" />
    <a-card class="general-card" title="中医证候管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="证候名或拼音" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="syndromeCode" label="证候编码:">
                  <a-input v-model="searchData.syndromeCode" placeholder="请输入证候编码" />
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
            <a-button v-if="buttons.includes('syndrome:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加中医证候
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
        :data="syndromes.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button v-if="buttons.includes('syndrome:modify')" type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此证候?" @ok="handleDelete(record.id)" v-if="buttons.includes('syndrome:delete')">
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 证候编辑窗 -->
    <SyndromeSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { SyndromeView, SyndromeSearchModel, getSyndromePagination, deleteSyndrome } from '@/api/syndrome'
import SyndromeSingle from './components/single.vue'

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

// region 证候列表
// 表格列定义
const columns = [
  {
    title: '证候编码',
    dataIndex: 'syndromeCode',
  },
  {
    title: '证候名称',
    dataIndex: 'syndromeName',
  },
  {
    title: '描述',
    dataIndex: 'description',
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
    syndromeCode: '',
    deleted: false,
  }
}

const reset = () => {
  searchData.value = generateSearchModel()
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
    items: [] as SyndromeView[],
  }
}

const searchData = ref<SyndromeSearchModel>(generateSearchModel())
const syndromes = ref<Pagination<SyndromeView>>(generateDatalist())

const load = async () => {
  syndromes.value = (await getSyndromePagination(searchData.value, pager.value)) || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: syndromes.value.total,
    pageSize: syndromes.value.pageSize,
    current: syndromes.value.page,
    totalPage: syndromes.value.totalPage,
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
  deleteSyndrome(id).then((res) => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'SyndromeManage',
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
