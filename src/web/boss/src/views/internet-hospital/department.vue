<template>
  <div class="container">
    <Breadcrumb :items="['互联网医院', '互医科室']" page-name="互医科室" route-name="InternetHospisalDepartmentManage" />
    <a-card class="general-card" title="互联网医院科室">
      <a-divider style="margin-top: 0" />
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :flex="'320px'">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="输入科室名称搜索" />
                </a-form-item>
              </a-col>
              <a-col :flex="'320px'">
                <a-form-item field="departmentType" label="科室类型:">
                  <a-select v-model="searchData.departmentType" placeholder="请选择科室类型" allow-clear>
                    <a-option v-for="item in departmentTypes" :value="item.code">{{ item.name }}</a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 28px" direction="vertical" />
        <a-col :flex="'200px'" style="text-align: right">
          <a-space :size="18">
            <a-button @click="load" type="primary" size="small">
              <template #icon><icon-search /></template>
              搜索
            </a-button>
            <a-button @click="reset" size="small">
              <template #icon><icon-refresh /></template>
              重置
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-divider style="margin-top: 0" />
      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button v-if="buttons.includes('internet-hospital-department:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon><icon-plus /></template>
              添加科室
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon" @click="load"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>

      <a-table style="margin-bottom: 16px" :columns="columns" :data="departments" :bordered="false" :pagination="false" :loading="loading">
        <template #optional="{ record }">
          <a-space>
            <a-button v-if="buttons.includes('internet-hospital-department:modify')" type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm
              content="确定删除此权限组?"
              @ok="handleDelete(record.id)"
              v-if="buttons.includes('internet-hospital-department:delete') && record.children.length === 0"
            >
              <a-button type="text" size="mimi" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <DepartmentSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import {ref, computed, unref} from 'vue'
import { Message } from '@arco-design/web-vue'
import { ValueObject } from '@/api/common'
import { useRoute } from 'vue-router'
import { DepartmentView, DepartmentSearchModel, getDepartmentTypes, getDepartmentList, deleteDepartment } from '@/api/department'
import DepartmentSingle from './components/department-single.vue'

const route = useRoute()
const buttons = route.meta.buttons || []

// region 获取科室类型
const departmentTypes = ref<ValueObject[]>([])
const loadDepartmentTypes = async () => {
  const res = (await getDepartmentTypes()) || []
  departmentTypes.value = res
}
loadDepartmentTypes()
// endregion

// region 添加与编辑
const single = ref({
  open: false,
  action: 'add',
  id: '0',
})

const handleOpenSingle = (actionValue: string, idValue: string) => {
  single.value = {
    open: true,
    action: actionValue,
    id: idValue,
  }
}

const handleSuccess = () => {
  load()
}
// endregion

// region 搜索与表格数据
const searchData = ref<DepartmentSearchModel>({
  keywords: '',
  departmentType: '',
})

const reset = () => {
  searchData.value = {
    keywords: '',
    departmentType: '',
  }
  load()
}

const departments = ref<DepartmentView[]>([])

const loading = ref(false)

const load = async () => {
  loading.value = true
  const res = (await getDepartmentList(searchData.value)) || []
  departments.value = res
  loading.value = false
}

load()

const columns = [
  {
    title: '科室名称',
    dataIndex: 'departmentName',
  },
  {
    title: '科室类型',
    dataIndex: 'departmentType.name',
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

const handleDelete = async (id: string) => {
  deleteDepartment(id).then(() => {
    Message.success('删除成功')
    load()
  })
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'InternetHospitalDepartmentManage',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
