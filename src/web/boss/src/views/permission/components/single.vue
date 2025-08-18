<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="groupId" label="权限组 : ">
          <a-cascader v-model="formData.groupId" :options="groupOptions" placeholder="请选择" />
        </a-form-item>
        <a-form-item field="permissionName" label="权限名称 : ">
          <a-input v-model="formData.permissionName" placeholder="字母及-及:" />
        </a-form-item>
        <a-form-item field="displayName" label="权限显示名称 : ">
          <a-input v-model="formData.displayName" placeholder="汉字" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" row="3" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { CascaderOption } from '@/api/common'
import { PermissionGroupView, getPermissionGroupList } from '@/api/permission-group'
import { Permission, getPermission, createPermission, updatePermission } from '@/api/permission'

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

const dialog = ref({
  open: false,
  title: '添加权限',
  remark: '权限用于后端接口控制。',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      loadPermissionGroupDataList()
      load()
    }
  }
)

// region 加载权限
const load = async () => {
  formData.value = props.action === 'add' ? generate() : (await getPermission(props.singleId)) || generate()
}
// endregion

// region 加载权限组
const groupOptions = ref<CascaderOption[]>([])

const loadPermissionGroupDataList = async () => {
  const groups = (await getPermissionGroupList()) || []
  if (groups.length > 0) {
    const options = []
    for (let i = 0; i < groups.length; i += 1) {
      const item: PermissionGroupView = groups[i]
      const cs = []
      if (item.children.length > 0) {
        for (let m = 0; m < item.children.length; m += 1) {
          const child = item.children[m]
          cs.push({
            value: child.id,
            label: child.displayName,
          })
        }
      }

      if (cs.length > 0) {
        options.push({
          value: item.id,
          label: item.displayName,
          children: cs,
        })
      } else {
        options.push({
          value: item.id,
          label: item.displayName
        })
      }
    }
    groupOptions.value = options
  }
}
// endregion

// region 表单定义
const generate = () => {
  return {
    id: '0',
    groupId: '',
    permissionName: '',
    displayName: '',
    description: '',
  }
}

const formData = ref<Permission>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  groupId: [{ required: true, message: '请选择权限组' }],
  permissionName: [
    { required: true, message: '请输入权限名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
    { match: /^[a-zA-Z-\\:]+$/g, message: '只能由字母 - : 组成' },
  ],
  displayName: [
    { required: true, message: '请输入权限显示名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
  ],
  description: [{ maxLength: 255, message: '不能超过最大长度255个字符' }],
}
// endregion

// region 事件相应
// 表单验证
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

// 表单提交
const handleSubmit = async () => {
  if (props.action === 'add') {
    createPermission(formData.value).then(() => {
      Message.success(`创建权限成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updatePermission(formData.value).then(() => {
      Message.success('修改权限信息成功')
      handleClose()
      emit('on-success')
    })
  }
}

// 关闭
const handleClose = async () => {
  generate()
  emit('update:open', false)
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'PermissionSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
