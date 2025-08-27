<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark" v-if="dialog.remark !== ''">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="roleName" label="角色名称 : ">
          <a-input v-model="formData.roleName" placeholder="字母及-及:" />
        </a-form-item>
        <a-form-item field="displayName" label="角色显示名称 : ">
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
import { Role, getRole, createRole, updateRole } from '@/api/role'

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
  title: '添加角色',
  remark: '',
})

watch(
  () => props.open,
  (obj) => {
    dialog.value.open = props.open
    dialog.value.title = props.action === 'add' ? '添加角色' : '修改角色'
    load()
  }
)

// region 表单定义
const generate = () => {
  return {
    id: '0',
    roleName: '',
    displayName: '',
    description: '',
  }
}

const rules = {
  roleName: [
    { required: true, message: '请输入角色名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
    { match: /^[a-zA-Z-\\:]+$/g, message: '只能由字母 - : 组成' },
  ],
  displayName: [
    { required: true, message: '请输入角色显示名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
  ],
  description: [{ maxLength: 255, message: '不能超过最大长度255个字符' }],
}

const formData = ref<Role>(generate())
const formRef = ref<FormInstance | null>(null)

const load = () => {
  if (props.action === 'add') {
    formData.value = generate()
  } else {
    getRole(props.singleId)
      .then((res) => {
        formData.value = res
      })
      .catch((err) => {
        formData.value = generate()
        handleClose()
      })
  }
}
// endregion

// region 表单事件
// 关闭
const handleClose = () => {
  emit('update:open', false)
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
    createRole(unref(formData)).then(() => {
      Message.success('添加角色成功')
      handleClose()
      emit('on-success')
    })
  } else {
    updateRole(unref(formData)).then(() => {
      Message.success('修改角色信息成功')
      handleClose()
      emit('on-success')
    })
  }
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'RoleSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
