<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="pieceCode" label="饮片编码 : ">
          <a-input v-model="formData.pieceCode" placeholder="请输入饮片编码" />
        </a-form-item>
        <a-form-item field="pieceName" label="饮片名称 : ">
          <a-input v-model="formData.pieceName" placeholder="请输入饮片名称" />
        </a-form-item>
        <a-form-item field="pieceAlias" label="饮片别名 : ">
          <a-input v-model="formData.pieceAlias" placeholder="请输入饮片别名" />
        </a-form-item>
        <a-form-item field="nature" label="性味 : ">
          <a-input v-model="formData.nature" placeholder="请输入性味" />
        </a-form-item>
        <a-form-item field="meridian" label="归经 : ">
          <a-input v-model="formData.meridian" placeholder="请输入归经" />
        </a-form-item>
        <a-form-item field="indications" label="功能和主治 : ">
          <a-textarea v-model="formData.indications" placeholder="请输入功能和主治" />
        </a-form-item>
        <a-form-item field="dosage" label="用法用量 : ">
          <a-textarea v-model="formData.dosage" placeholder="请输入用法用量" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { ChineseMedicinePiece, getChineseMedicinePiece, createChineseMedicinePiece, updateChineseMedicinePiece } from '@/api/chinese-medicine-piece'

const props = defineProps<{
  open: boolean
  action: string
  singleId: string
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加中药饮片',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      load()
    }
  }
)

const load = async () => {
  formData.value = props.action === 'add' ? generate() : (await getChineseMedicinePiece(props.singleId))?.data || generate()
}

const generate = () => {
  return {
    id: '0',
    pieceCode: '',
    pieceName: '',
    pieceAlias: '',
    nature: '',
    meridian: '',
    indications: '',
    dosage: '',
  }
}

const formData = ref<ChineseMedicinePiece>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  pieceCode: [{ required: true, message: '请输入饮片编码' }],
  pieceName: [{ required: true, message: '请输入饮片名称' }],
}

const handleValidate = async () => {
  const v = await new Promise((resolve) => {
    formRef.value?.validate((r) => {
      if (r === undefined) {
        resolve(true)
      } else {
        Message.error(Object.values(r)[0].message)
        resolve(false)
      }
    })
  })
  return v
}

const handleSubmit = async () => {
  if (props.action === 'add') {
    createChineseMedicinePiece(formData.value).then(() => {
      Message.success('创建成功')
      handleClose()
      emit('on-success')
    })
  } else {
    updateChineseMedicinePiece(formData.value).then(() => {
      Message.success('修改成功')
      handleClose()
      emit('on-success')
    })
  }
}

const handleClose = async () => {
  formData.value = generate()
  emit('update:open', false)
}
</script>

<script lang="ts">
export default {
  name: 'ChineseMedicinePieceSingle',
}
</script>
