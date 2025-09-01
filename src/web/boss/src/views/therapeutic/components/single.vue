<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="therapeuticsCode" label="治法编码 : ">
          <a-input v-model="formData.therapeuticsCode" placeholder="请输入治法编码" />
        </a-form-item>
        <a-form-item field="therapeuticsName" label="治法名称 : ">
          <a-input v-model="formData.therapeuticsName" placeholder="请输入治法名称" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" row="3" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { Therapeutic, getTherapeutic, createTherapeutic, updateTherapeutic } from '@/api/therapeutic'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  action: string // 行为 add or modify
  singleId: string // 治法Id
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加治法',
  remark: '治法是中医诊断和治疗的重要组成部分。',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      if (props.action === 'modify') {
        dialog.value.title = '修改治法'
        load()
      } else {
        dialog.value.title = '添加治法'
        formData.value = generate()
      }
    }
  }
)

// region 加载治法
const load = async () => {
  formData.value = props.action === 'add' ? generate() : (await getTherapeutic(props.singleId)) || generate()
}
// endregion

// region 表单定义
const generate = () => {
  return {
    id: '0',
    therapeuticsCode: '',
    therapeuticsName: '',
    description: '',
  }
}

const formData = ref<Therapeutic>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  therapeuticsCode: [
    { required: true, message: '请输入治法编码' },
    { maxLength: 20, message: '不能超过最大长度20个字符' },
  ],
  therapeuticsName: [
    { required: true, message: '请输入治法名称' },
    { maxLength: 255, message: '不能超过最大长度255个字符' },
  ],
  description: [{ maxLength: 512, message: '不能超过最大长度512个字符' }],
}
// endregion

// region 事件相应
// 表单验证
const handleValidate = async () => {
  const v = await new Promise((resolve) => {
    formRef.value?.validate((r) => {
      if (r === undefined) {
        resolve(true)
      } else {
        const firstError = Object.values(r)[0];
        if (firstError && firstError.message) {
          Message.error(firstError.message);
        }
        resolve(false);
      }
    })
  })

  return v
}

// 表单提交
const handleSubmit = async () => {
  if (props.action === 'add') {
    createTherapeutic(formData.value).then(() => {
      Message.success(`创建治法成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateTherapeutic(formData.value).then(() => {
      Message.success('修改治法信息成功')
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
  name: 'TherapeuticSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
