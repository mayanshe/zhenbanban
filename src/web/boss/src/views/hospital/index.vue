<template>
  <div class="container">
    <Breadcrumb :items="['业务', '医院管理']" page-name="业务医院" route-name="HospitalManage" />
    <a-card class="general-card" title="业务医院管理">
      <a-divider style="margin-top: 0" />

      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="医院名或编码" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="hospitalCode" label="医院编码:">
                  <a-input v-model="searchData.hospitalCode" placeholder="请输入医院编码" />
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
              添加业务医院
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip :content="`显示/隐藏列`">
            <a-popover trigger="click" position="bl" @popup-visible-change="popupVisibleChange">
              <div class="action-icon"><icon-settings size="18" /></div>
              <template #content>
                <div id="tableSetting">
                  <div v-for="(item, index) in showColumns" :key="item.dataIndex" class="setting">
                    <div style="margin-right: 4px; cursor: move">
                      <icon-drag-arrow />
                    </div>
                    <div>
                      <a-checkbox v-model="item.checked" @change="handleChange($event, item as TableColumnData, index)"></a-checkbox>
                    </div>
                    <div class="title">
                      {{ item.title === '#' ? '序列号' : item.title }}
                    </div>
                  </div>
                </div>
              </template>
            </a-popover>
          </a-tooltip>
          <a-tooltip content="刷新">
            <div class="action-icon" @click="search">
              <icon-refresh size="18" />
            </div>
          </a-tooltip>
        </a-col>
      </a-row>

      <a-table
        style="margin-bottom: 16px"
        :columns="cloneColumns as TableColumnData[]"
        :data="hospitals.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">编辑</a-button>
            <a-popconfirm content="确定删除此业务医院?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <HospitalSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'
import type { TableColumnData } from '@arco-design/web-vue/es/table/interface'
import cloneDeep from 'lodash/cloneDeep'
import Sortable from 'sortablejs'
import { Pager, Pagination } from '@/api/common'
import { HospitalView, HospitalSearchModel, getHospitalPagination, deleteHospital } from '@/api/hospital'
import HospitalSingle from './components/single.vue'

const route = useRoute()
const buttons = route.meta.buttons || []
type Column = TableColumnData & { checked?: true }

// region 编辑页
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

// region 加载业务医院列表
const generateSearchModel = (): HospitalSearchModel => {
  return {
    keywords: '',
    hospitalCode: '',
    deleted: false,
  }
}

const columns = computed<TableColumnData[]>(() => [
  { title: '医院名称', dataIndex: 'hospitalName', width: 250 },
  { title: '所有制', dataIndex: 'ownershipType.name', width: 90 },
  { title: '类型', dataIndex: 'hospitalType.name', width: 90 },
  { title: '等级', dataIndex: 'hospitalLevel.name', width: 70 },
  { title: '省份', dataIndex: 'province' },
  { title: '城市', dataIndex: 'city' },
  { title: '区县', dataIndex: 'county' },
  { title: '状态', dataIndex: 'status.name' },
  { title: '创建日期', dataIndex: 'createdAt' },
  { title: '修改日期', dataIndex: 'updatedAt' },
  { title: '操作', slotName: 'optional' },
])

const pager = ref<Pager>({
  page: 1,
  pageSize: 10,
})

const generateDatalist = (): Pagination<HospitalView> => {
  return {
    page: 1,
    pageSize: 10,
    count: 0,
    total: 0,
    totalPage: 0,
    prevPage: 1,
    nextPage: 1,
    items: [],
  }
}

const searchData = ref<HospitalSearchModel>(generateSearchModel())
const hospitals = ref<Pagination<HospitalView>>(generateDatalist())

const load = async () => {
  const response = await getHospitalPagination(searchData.value, pager.value)
  hospitals.value = response || generateDatalist()
}

const search = async () => {
  pager.value.page = 1
  load()
}

load()

const pagination = computed(() => {
  return {
    total: hospitals.value.total,
    pageSize: hospitals.value.pageSize,
    current: hospitals.value.page,
  }
})
// endregion

const reset = () => {
  searchData.value = generateSearchModel()
}

const handlePageChange = (page: number) => {
  pager.value.page = page
  load()
}

const handleDelete = async (id: string) => {
  deleteHospital(id).then(() => {
    Message.success('删除成功')
    search()
  })
}

const cloneColumns = ref<Column[]>([])
const showColumns = ref<Column[]>([])
const handleChange = (checked: boolean | (string | boolean | number)[], column: Column, index: number) => {
  if (!checked) {
    cloneColumns.value = showColumns.value.filter((item) => item.dataIndex !== column.dataIndex)
  } else {
    cloneColumns.value.splice(index, 0, column)
  }
}

const exchangeArray = <T extends Array<any>>(array: T, beforeIdx: number, newIdx: number, isDeep = false): T => {
  const newArray = isDeep ? cloneDeep(array) : array
  if (beforeIdx > -1 && newIdx > -1) {
    // 先替换后面的，然后拿到替换的结果替换前面的
    newArray.splice(beforeIdx, 1, newArray.splice(newIdx, 1, newArray[beforeIdx]).pop())
  }
  return newArray
}

const popupVisibleChange = (val: boolean) => {
  if (val) {
    nextTick(() => {
      const el = document.getElementById('tableSetting') as HTMLElement
      const sortable = new Sortable(el, {
        onEnd(e: any) {
          const { oldIndex, newIndex } = e
          exchangeArray(cloneColumns.value, oldIndex, newIndex)
          exchangeArray(showColumns.value, oldIndex, newIndex)
        },
      })
    })
  }
}

watch(
  () => columns.value,
  (val) => {
    cloneColumns.value = cloneDeep(val)
    cloneColumns.value.forEach((item, index) => {
      item.checked = true
    })
    showColumns.value = cloneDeep(cloneColumns.value)
  },
  { deep: true, immediate: true }
)
</script>

<script lang="ts">
export default {
  name: 'HospitalManage',
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