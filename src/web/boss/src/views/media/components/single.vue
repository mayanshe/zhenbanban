<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'

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
  title: '添加新文件',
  remark: '权限用于后端接口控制。',
})

watch(
    () => props.open,
    (val) => {
      dialog.value.open = val
    }
)

// region 响应
const handleValidate = async () => {

}

const handleSubmit = async () => {

}

const handleClose = async () => {
  emit('update:open', false)
}
// endregion
</script>