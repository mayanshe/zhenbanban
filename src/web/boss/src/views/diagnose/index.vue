<template>
  <div class="container">
    <Breadcrumb :items="['数据字典', '疾病诊断']" />
    <a-card class="general-card" title="疾病诊断管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="编码、名称、拼音、别名等" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="icdType" label="ICD类型:">
                  <a-select v-model="searchData.icdType" placeholder="请选择" allow-clear>
                    <a-option :value="1">国临2.0</a-option>
                    <a-option :value="2">中医GB/T15657-2021</a-option>
                    <a-option :value="3">医保2.0</a-option>
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
        :data="diagnoses.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此诊断?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <DiagnoseSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { DiagnoseView, DiagnoseSearchModel, getDiagnosePagination, deleteDiagnose } from '@/api/diagnose'
import DiagnoseSingle from './components/single.vue'

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

// region 列表
const columns = [
  { title: 'ICD编码', dataIndex: 'icdCode' },
  { title: 'ICD名称', dataIndex: 'icdName' },
  { title: 'ICD类型', dataIndex: 'icdType',
    render: (data: any) => {
        const record = data.record as DiagnoseView;
        switch (record.icdType) {
            case 1:
                return '国临2.0';
            case 2:
                return '中医GB/T15657-2021';
            case 3:
                return '医保2.0';
            default:
                return '未知';
        }
    }
   },
  { title: '创建时间', dataIndex: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt' },
  { title: '操作', slotName: 'optional' },
]

const pager = ref<Pager>({
  page: 1,
  pageSize: 15,
})

const generateSearchModel = (): DiagnoseSearchModel => {
  return {
    keywords: '',
    icdType: null,
  }
}

const reset = () => {
  searchData.value = generateSearchModel()
}

const generateDatalist = (): Pagination<DiagnoseView> => {
  return {
    page: 1,
    pageSize: 15,
    count: 0,
    total: 0,
    totalPage: 0,
    prevPage: 1,
    nextPage: 1,
    items: [],
  }
}

const searchData = ref<DiagnoseSearchModel>(generateSearchModel())
const diagnoses = ref<Pagination<DiagnoseView>>(generateDatalist())

const load = async () => {
  diagnoses.value = (await getDiagnosePagination(searchData.value, pager.value)) || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: diagnoses.value.total,
    pageSize: diagnoses.value.pageSize,
    current: diagnoses.value.page,
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
  deleteDiagnose(id).then(() => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'DiagnoseManage',
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
