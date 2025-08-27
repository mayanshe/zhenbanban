<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="icdType" label="ICD类型 : ">
          <a-select v-model="formData.icdType" placeholder="请选择">
            <a-option :value="1">国临2.0</a-option>
            <a-option :value="2">中医GB/T15657-2021</a-option>
            <a-option :value="3">医保2.0</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="icdCode" label="ICD编码 : ">
          <a-input v-model="formData.icdCode" placeholder="请输入ICD编码" />
        </a-form-item>
        <a-form-item field="icdName" label="ICD名称 : ">
          <a-input v-model="formData.icdName" placeholder="请输入ICD名称" />
        </a-form-item>
        <a-form-item field="icdNamePinyin" label="ICD名称拼音 : ">
          <a-input v-model="formData.icdNamePinyin" placeholder="请输入ICD名称拼音" />
        </a-form-item>
        <a-form-item field="icdNamePinyinAbbr" label="ICD名称拼音首字母 : ">
          <a-input v-model="formData.icdNamePinyinAbbr" placeholder="请输入ICD名称拼音首字母" />
        </a-form-item>
        <a-form-item field="icdOptionalName" label="ICD可选名称 : ">
          <a-input v-model="formData.icdOptionalName" placeholder="请输入ICD可选名称" />
        </a-form-item>
        <a-form-item field="icdOptionalNamePinyin" label="ICD可选名称拼音 : ">
          <a-input v-model="formData.icdOptionalNamePinyin" placeholder="请输入ICD可选名称拼音" />
        </a-form-item>
        <a-form-item field="icdOptionalNamePinyinAbbr" label="ICD可选名称拼音首字母 : ">
          <a-input v-model="formData.icdOptionalNamePinyinAbbr" placeholder="请输入ICD可选名称拼音首字母" />
        </a-form-item>
        <a-form-item field="icdAliasName" label="ICD别名 : ">
          <a-input v-model="formData.icdAliasName" placeholder="请输入ICD别名" />
        </a-form-item>
        <a-form-item field="icdAliasNamePinyin" label="ICD别名拼音 : ">
          <a-input v-model="formData.icdAliasNamePinyin" placeholder="请输入ICD别名拼音" />
        </a-form-item>
        <a-form-item field="icdAliasNamePinyinAbbr" label="ICD别名拼音首字母 : ">
          <a-input v-model="formData.icdAliasNamePinyinAbbr" placeholder="请输入ICD别名拼音首字母" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" row="3" />
        </a-form-item>
        <a-form-item field="chapterCode" label="章节编码 : ">
          <a-input v-model="formData.chapterCode" placeholder="请输入章节编码" />
        </a-form-item>
        <a-form-item field="chapterName" label="章节名称 : ">
          <a-input v-model="formData.chapterName" placeholder="请输入章节名称" />
        </a-form-item>
        <a-form-item field="blockCode" label="疾病组编码 : ">
          <a-input v-model="formData.blockCode" placeholder="请输入疾病组编码" />
        </a-form-item>
        <a-form-item field="blockName" label="疾病组名称 : ">
          <a-input v-model="formData.blockName" placeholder="请输入疾病组名称" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { Diagnose, getDiagnose, createDiagnose, updateDiagnose } from '@/api/diagnose'

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
  title: '添加疾病诊断',
  remark: '请填写疾病诊断的详细信息。',
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

// region 加载
const load = async () => {
  formData.value = props.action === 'add' ? generate() : (await getDiagnose(props.singleId)) || generate()
}
// endregion

// region 表单定义
const generate = (): Diagnose => {
  return {
    id: '0',
    icdType: 1,
    icdCode: '',
    icdName: '',
    icdNamePinyin: '',
    icdNamePinyinAbbr: '',
    icdOptionalName: '',
    icdOptionalNamePinyin: '',
    icdOptionalNamePinyinAbbr: '',
    icdAliasName: '',
    icdAliasNamePinyin: '',
    icdAliasNamePinyinAbbr: '',
    description: '',
    chapterCode: '',
    chapterName: '',
    blockCode: '',
    blockName: '',
  }
}

const formData = ref<Diagnose>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  icdType: [{ required: true, message: '请选择ICD类型' }],
  icdCode: [{ required: true, message: '请输入ICD编码' }],
  icdName: [{ required: true, message: '请输入ICD名称' }],
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
    createDiagnose(formData.value).then(() => {
      Message.success(`创建成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateDiagnose(formData.value).then(() => {
      Message.success('修改成功')
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
  name: 'DiagnoseSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
