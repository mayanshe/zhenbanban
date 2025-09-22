<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="parentId" label="上级科室 : ">
          <a-cascader v-model="formData.parentId" :options="departmentOptions" placeholder="请选择 (可为空)" allow-clear check-strictly />
        </a-form-item>
        <a-form-item field="departmentName" label="科室名称 : ">
          <a-input v-model="formData.departmentName" placeholder="请输入科室名称" />
        </a-form-item>
        <a-form-item field="departmentType" label="科室类型 : ">
          <a-select v-model="formData.departmentType" placeholder="请选择科室类型">
            <a-option v-for="item in departmentTypes" :value="item.code">{{ item.name }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="summary" label="科室简介 : ">
          <a-textarea v-model="formData.summary" row="3" />
        </a-form-item>
        <a-form-item field="description" label="科室介绍 : ">
          <a-textarea v-model="formData.description" row="5" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { CascaderOption, ValueObject } from '@/api/common'
import {
  Department,
  getDepartment,
  createDepartment,
  updateDepartment,
  getDepartmentList,
  getDepartmentTypes,
  DepartmentView,
} from '@/api/department'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  action: string // 行为 add or modify
  singleId: string // 科室Id
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加科室',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      dialog.value.title = props.action === 'add' ? '添加科室' : '修改科室'
      loadDepartmentOptions()
      load()
    }
  }
)

// region 加载科室类型
const departmentTypes = ref<ValueObject[]>([])
const loadDepartmentTypes = async () => {
  const res = (await getDepartmentTypes()) || []
  departmentTypes.value = res
}
loadDepartmentTypes()
// endregion

// region 加载科室数据
const load = async () => {
  if (props.action === 'add') {
    formData.value = generate()
  } else {
    const resp = await getDepartment(props.singleId)
    if (resp) {
      formData.value = {
        id: resp.id,
        parentId: resp.parentId,
        departmentType: resp.departmentType.code,
        departmentName: resp.departmentName,
        summary: resp.summary,
        description: resp.description,
      }
    } else {
      formData.value = generate()
      Message.error('获取科室信息失败，请稍后重试')
      handleClose()
    }
  }
}
// endregion

// region 加载上级科室选项
const departmentOptions = ref<CascaderOption[]>([])

const transDepartments = (departments: DepartmentView[]): CascaderOption[] => {
  return departments.map((dept) => ({
    label: dept.departmentName,
    value: dept.id,
    children: dept.children && dept.children.length > 0 ? transDepartments(dept.children) : null,
  }))
}

const loadDepartmentOptions = async () => {
  const departments = await getDepartmentList({ keywords: '', departmentType: '' })
  if (departments.length === 0) {
    departmentOptions.value = [{ label: '无', value: '0', children: null }]
    return
  }

  departmentOptions.value = [{ label: '无', value: '0', children: null }, ...transDepartments(departments)]
}
loadDepartmentOptions()
// endregion

// region 表单定义
const generate = (): Department => {
  return {
    id: '0',
    parentId: '0',
    departmentType: '',
    departmentName: '',
    summary: '',
    description: '',
  }
}

const formData = ref<Department>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  departmentName: [{ required: true, message: '请输入科室名称' }],
  departmentType: [{ required: true, message: '请选择科室类型' }],
}
// endregion

// region 事件相应
const handleValidate = async (): Promise<boolean> => {
  const res = await formRef.value?.validate()
  if (res) {
    Message.error(Object.values(res)[0].message)
    return false
  }
  return true
}

const handleSubmit = async () => {
  const data = { ...formData.value }
  if (!data.parentId) {
    data.parentId = '0'
  }

  if (props.action === 'add') {
    createDepartment(data).then(() => {
      Message.success(`创建科室成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateDepartment(data).then(() => {
      Message.success('修改科室信息成功')
      handleClose()
      emit('on-success')
    })
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  emit('update:open', false)
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'DepartmentSingle',
}
</script>
