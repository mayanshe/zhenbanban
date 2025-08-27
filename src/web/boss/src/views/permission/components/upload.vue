<template>
  <a-modal :width="500" v-model:visible="dialog.open" @close="handleClose" @ok="handleUpload" unmountOnClose>
    <template #title>
      <div class="title">
        添加新闻而头脑
      </div>
    </template>
    <div>
      <a-upload action="/">
        <template #upload-button>
          <div
              style="
        background-color: var(--color-fill-2);
        color: var(--color-text-1);
        border: 1px dashed var(--color-fill-4);
        height: 158px;
        width: 380px;
        border-radius: 2px;
        line-height: 158px;
        text-align: center;"
          >
            <div>
              将文件拖到框内或
              <span style="color: #3370FF"> 点击上传</span>
            </div>
          </div>
        </template>
      </a-upload>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { Message } from '@arco-design/web-vue'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  remark: '此处上传的文件均为私密读写。',
})

watch(
    () => props.open,
    (val) => {
      dialog.value.open = props.open
    }
)

const handleClose = async () => {
  emit('update:open', false)
}

const handleUpdate = async () => {

}

</script>

<style scoped lang="less">
</style>