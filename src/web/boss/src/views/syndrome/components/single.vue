<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="syndromeCode" label="证候编码 : ">
          <a-input v-model="formData.syndromeCode" placeholder="请输入证候编码" />
        </a-form-item>
        <a-form-item field="syndromeName" label="证候名称 : ">
          <a-input v-model="formData.syndromeName" placeholder="请输入证候名称" />
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
import { Syndrome, getSyndrome, createSyndrome, updateSyndrome } from '@/api/syndrome'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  action: string // 行为 add or modify
  singleId: string // 证候Id
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加证候',
  remark: '中医证候是构成临床各种具体病证的基本要素。',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      if (props.action === 'modify') {
        dialog.value.title = '修改证候'
        load()
      } else {
        dialog.value.title = '添加证候'
        formData.value = generate()
      }
    }
  }
)

// region 加载证候
const load = async () => {
  formData.value = (await getSyndrome(props.singleId)) || generate()
}
// endregion

// region 表单定义
const generate = () => {
  return {
    id: '0',
    syndromeCode: '',
    syndromeName: '',
    description: '',
  }
}

const formData = ref<Syndrome>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  syndromeCode: [
    { required: true, message: '请输入证候编码' },
    { maxLength: 20, message: '不能超过最大长度20个字符' },
  ],
  syndromeName: [
    { required: true, message: '请输入证候名称' },
    { maxLength: 255, message: '不能超过最大长度255个字符' },
  ],
  description: [{ maxLength: 512, message: '不能超过最大长度512个字符' }],
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
    createSyndrome(formData.value).then(() => {
      Message.success(`创建证候成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateSyndrome(formData.value).then(() => {
      Message.success('修改证候信息成功')
      handleClose()
      emit('on-success')
    })
  }
}

// 关闭
const handleClose = async () => {
  formData.value = generate()
  formRef.value?.clearValidate()
  emit('update:open', false)
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'SyndromeSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
