<template>
  <div class="container">
    <Breadcrumb :items="['字典', '西药/中成药']" />
    <a-card class="general-card" title="西药/中成药管理">
      <a-divider style="margin-top: 0" />
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="6">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="商品名, 注册名, 拼音..." />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="medicineCode" label="编码:">
                  <a-input v-model="searchData.medicineCode" placeholder="请输入药品编码" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="icd" label="OTC类?:">
                  <a-select v-model="searchData.icd" placeholder="请选择">
                    <a-option :value="true">是</a-option>
                    <a-option :value="false">否</a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item field="poisonous" label="毒麻类?:">
                  <a-select v-model="searchData.poisonous" placeholder="请选择">
                    <a-option :value="true">是</a-option>
                    <a-option :value="false">否</a-option>
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
            <a-button v-if="buttons.includes('medicine:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加西药/中成药
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
        :data="medicines.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button v-if="buttons.includes('medicine:modify')" type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此药品?" @ok="handleDelete(record.id)" v-if="buttons.includes('medicine:delete')">
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <MedicineSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import { Pager, Pagination } from '@/api/common'
import { MedicineView, MedicineSearchModel, getMedicinePagination, deleteMedicine } from '@/api/medicine'
import MedicineSingle from './components/single.vue'

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

// region 列表
const columns = [
  { title: '药品编码', dataIndex: 'medicineCode', width: 200 },
  { title: '注册名称', dataIndex: 'registeredName', width: 200 },
  { title: '实际剂型', dataIndex: 'realityMedicineModel', width: 120 },
  { title: '生产企业', dataIndex: 'companyName', width: 220 },
  { title: '商品名称', dataIndex: 'medicineName' },
  { title: '创建时间', dataIndex: 'createdAt' },
  { title: '修改时间', dataIndex: 'updatedAt' },
  { title: '操作', slotName: 'optional' },
]

const pager = ref<Pager>({
  page: 1,
  pageSize: 15,
})

const generateSearchModel = (): MedicineSearchModel => {
  return {
    keywords: '',
    medicineCode: '',
    deleted: false,
    icd: undefined,
    poisonous: undefined,
  }
}

const reset = () => {
  searchData.value = generateSearchModel()
}

const generateDatalist = (): Pagination<MedicineView> => {
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
    items: [] as MedicineView[],
  }
}

const searchData = ref<MedicineSearchModel>(generateSearchModel())
const medicines = ref<Pagination<MedicineView>>(generateDatalist())

const load = async () => {
  medicines.value = (await getMedicinePagination(searchData.value, pager.value)) || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: medicines.value.total,
    pageSize: medicines.value.pageSize,
    current: medicines.value.page,
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
  deleteMedicine(id).then(() => {
    Message.success('删除成功')
    search()
  })
}
</script>

<script lang="ts">
export default {
  name: 'MedicineManage',
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
