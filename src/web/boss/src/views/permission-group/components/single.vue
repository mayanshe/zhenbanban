<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="parentId" label="父权限组">
          <a-select placeholder="请选择" v-model="formData.parentId">
            <a-option v-for="(item, index) in rootPermissionGroups" :key="item.value" :value="item.value">{{ item.title }}</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="groupName" label="权限组名称 : ">
          <a-input v-model="formData.groupName" placeholder="字母及-及:" />
        </a-form-item>
        <a-form-item field="displayName" label="权限组显示名称 : ">
          <a-input v-model="formData.displayName" placeholder="汉字" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" row="3" />
        </a-form-item>
        <a-form-item field="sort" label="排序（按倒序） : ">
          <a-input v-model="formData.sort" placeholder="倒序" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import {
  PermissionGroup,
  PermissionGroupKeyValue,
  getPermissionGroup,
  getPermissionGroupList,
  createPermissionGroup,
  updatePermissionGroup,
  PermissionGroupView,
} from '@/api/permission-group'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  action: string // 行为 add or modify
  singleId: string // 权限组Id
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const formData = ref<PermissionGroup>({
  description: '',
  displayName: '',
  groupName: '',
  id: '0',
  parentId: '0',
  sort: '0',
  createdAt: '',
  updatedAt: '',
})
const formRef = ref<FormInstance | null>(null)

const rules = {
  parentId: [{ required: true, message: '请选择父权限组' }],
  groupName: [
    { required: true, message: '请输入权限组名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
    { match: /^[a-zA-Z\\-]+$/g, message: '只能由字母 - : 组成' },
  ],
  displayName: [
    { required: true, message: '请输入权限组显示名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
  ],
  description: [{ maxLength: 255, message: '不能超过最大长度255个字符' }],
  sort: [
    { required: true, message: '请输入权限排序' },
    { match: /^[0-9]\d*$/, message: '排序只能是非负整数' },
  ],
}

const dialog = ref({
  open: false,
  title: '添加权限组',
  remark: '权限组仅用于分组权限，不做其它用途。',
})

watch(
  () => props.open,
  (obj) => {
    dialog.value.open = props.open

    dialog.value.title = props.action === 'add' ? '添加权限组' : '修改权限组'

    if (props.action === 'add') {
      formData.value = generate()
    } else {
      load(props.singleId)
    }

    loadRootPermissionGroups()
  }
)

// region 加载根Key-Value形式
const rootPermissionGroups = ref<PermissionGroupKeyValue[]>([{ value: '0', title: '无' }])
const loadRootPermissionGroups = async () => {
  getPermissionGroupList().then((res) => {
    const list = [{ value: '0', title: '无' }]
    res.forEach((el: PermissionGroupView) => {
      list.push({ value: el.id, title: el.displayName })
    })
    rootPermissionGroups.value = list
  })
}
// endregion

// region 初始化对象实例
const generate = () => {
  return {
    id: '0',
    parentId: '0',
    groupName: '',
    displayName: '',
    description: '',
    sort: '0',
    createdAt: '',
    updatedAt: '',
  }
}

// 加载
const load = async (id: string) => {
  getPermissionGroup(id)
    .then((res) => {
      formData.value = res
    })
    .catch((err) => {
      formData.value = generate()
      handleClose()
    })
}
// endregion

// region 事件响应
// 关闭
const handleClose = () => {
  emit('update:open', false)
  emit('on-success')
}

// 验证
const handleValidate = async () => {
  const v = await new Promise((resolve) => {
    formRef.value?.validate((r, Record) => {
      if (r === undefined) {
        resolve(true)
      } else {
        Object.keys(r).forEach((key) => {
          Message.error(r[key].message)
        })
        resolve(false)
      }
    })
  })

  return v
}

// 提交
const handleSubmit = async () => {
  if (props.action === 'add') {
    createPermissionGroup(formData.value).then(() => {
      Message.success(`创建权限组成功`)
      handleClose()
    })
  } else {
    updatePermissionGroup(formData.value).then(() => {
      Message.success('修改权限组信息成功')
      handleClose()
    })
  }
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'PermissionGroupSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
