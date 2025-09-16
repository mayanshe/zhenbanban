<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="pieceId" label="中药饮片 :">
          <a-select
            v-model="formData.pieceId"
            placeholder="输入搜索"
            :allow-search="true"
            @search="handelSearchPieceOptions"
            :filter-option="false"
            :loading="pieceOptionLoading"
          >
            <a-option v-for="item in pieceOptions" :value="item.id">{{ item.name }}({{ item.code }}, {{ item.alias }})</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="kind" label="禁忌类型 :">
          <a-select v-model="formData.kind" placeholder="请选择">
            <a-option :value="18">十八反</a-option>
            <a-option :value="19">十九畏</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="conflictPieceId" label="配伍禁忌 :">
          <a-select
            v-model="formData.conflictPieceId"
            placeholder="输入搜索"
            :allow-search="true"
            @search="handleSearchConflictPieceOptions"
            :filter-option="false"
            :loading="conflictPieceOptionLoading"
          >
            <a-option v-for="item in conflictPieceOptions" :value="item.id">{{ item.name }}({{ item.code }}, {{ item.alias }})</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { Antagonism, getAntagonism, createAntagonism, updateAntagonism } from '@/api/antagonism'
import { ChineseMedicinePieceOption, getChineseMedicinePieceOptions } from '@/api/chinese-medicine-piece'

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
  title: '添加',
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      dialog.value.title = props.action === 'add' ? '添加十八反十九畏' : '修改十八反十九畏'
      load()
    }
  }
)

const load = async () => {
  if (props.action === 'modify' && props.singleId) {
    const res = await getAntagonism(props.singleId)

    pieceOptions.value = [
      {
        id: res.pieceId,
        code: res.pieceCode,
        name: res.pieceName,
        alias: res.pieceAlias,
      },
    ]

    conflictPieceOptions.value = [
      {
        id: res.conflictPieceId,
        code: res.conflictPieceCode,
        name: res.conflictPieceName,
        value: res.conflictPieceAlias,
      },
    ]

    formData.value.pieceId = res.pieceId
    formData.value.conflictPieceId = res.conflictPieceId
    formData.value.kind = res.kind
  } else {
    formData.value = generate()
  }
}

const generate = (): Antagonism => {
  return {
    id: '0',
    kind: 18,
    pieceId: '',
    conflictPieceId: '',
  }
}

const formData = ref<Antagonism>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  pieceId: [{ required: true, message: '请选择中药饮片' }],
  kind: [{ required: true, message: '请输入配伍禁忌类型' }],
  conflictPieceId: [{ required: true, message: '请选择禁忌配伍' }],
}

const handleValidate = async () => {
  const validation = await formRef.value?.validate()
  if (validation) {
    Message.error(Object.values(validation)[0].message)
    return false
  }
  return true
}

const handleSubmit = async () => {
  try {
    if (props.action === 'add') {
      await createAntagonism(formData.value)
      Message.success('创建成功')
    } else {
      await updateAntagonism(formData.value)
      Message.success('修改成功')
    }
    handleClose()
    emit('on-success')
  } catch (error) {
    // Error handling is managed by global axios interceptor
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  emit('update:open', false)
}

// region 获取中药饮片
const pieceOptions = ref<ChineseMedicinePieceOption[]>([])
const pieceOptionLoading = ref<boolean>(false)
const conflictPieceOptions = ref<ChineseMedicinePieceOption[]>([])
const conflictPieceOptionLoading = ref<boolean>(false)

const handelSearchPieceOptions = async (query: any) => {
  pieceOptions.value = []
  if (query) {
    pieceOptionLoading.value = true
    pieceOptions.value = (await getChineseMedicinePieceOptions(query)) || []
    pieceOptionLoading.value = false
  }
}

const handleSearchConflictPieceOptions = async (query: any) => {
  conflictPieceOptions.value = []
  if (query) {
    conflictPieceOptionLoading.value = true
    conflictPieceOptions.value = (await getChineseMedicinePieceOptions(query)) || []
    conflictPieceOptionLoading.value = false
  }
}

// endregion
</script>

<script lang="ts">
export default {
  name: 'AntagonismSingle',
}
</script>
