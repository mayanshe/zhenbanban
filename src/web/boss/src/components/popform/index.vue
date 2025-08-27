<template>
  <div class="pop-box" v-show="open" :style="{ width: boxw }">
    <div class="pop-form" :style="{ width: formw }">
      <!-- 标题区域 -->
      <a-layout-header class="pop-header">
        <a-typography-title :heading="5">{{ title }}</a-typography-title>
      </a-layout-header>

      <!-- 内容区域，支持滚动 -->
      <a-layout-content class="pop-body">
        <div class="pop-remark" v-if="remark != ''">
          {{ remark }}
        </div>
        <slot />
      </a-layout-content>

      <!-- 底部按钮区域 -->
      <a-layout-footer class="pop-footer">
        <a-space>
          <a-button type="primary" @click="handleSubmit">提交</a-button>
          &nbsp;
          <a-button @click="handleReset">重置</a-button>
          <a-button @click="handleClose">关闭</a-button>
        </a-space>
      </a-layout-footer>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watch } from 'vue'
import { Layout as ALayout, Space as ASpace, Switch as ASwitch, Typography, Button as AButton } from '@arco-design/web-vue'

// 定义组件的 props
const props = defineProps<{
  open: boolean // 接收 open prop 用于 v-model
  width?: string // 组件宽度，默认为 400px
  title: string // 标题
  remark?: string // 备注
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void // 支持 v-model
  (e: 'on-close'): void
  (e: 'on-reset'): void
  (e: 'on-submit'): void
}>()

// 同步 props.open 到本地状态ox
const open = ref(props.open)

const boxWidth = ref(0)
const formWidth = ref(0)

const boxw = computed(() => {
  const w = boxWidth.value >= 0 ? `${boxWidth.value}vw` : '100vw'
  return w
})

const formw = computed(() => {
  return formWidth.value >= 0 ? `${formWidth.value}px` : '420px'
})

const animateBox = () => {
  if (open) {
    setTimeout(function animate() {
      boxWidth.value += 20
      if (boxWidth.value < 100) {
        animateBox()
      }
    }, 5)
  } else {
    boxWidth.value = 0
  }
}

const animateForm = (limit: number) => {
  if (open) {
    setTimeout(function animate() {
      formWidth.value += 10
      if (formWidth.value <= limit) {
        animateForm(limit)
      }
    }, 1)
  } else {
    formWidth.value = 0
  }
}

// 监听 props.open 的变化，保持同步
watch(
  () => props.open,
  (newVal) => {
    open.value = newVal

    if (newVal) {
      animateBox()
      const num = props?.width?.replace(/[^[0-9]+/g, '') || '420'
      const l = parseInt(num, 10)
      animateForm(l)
    }
  }
)

// 处理关闭按钮
const handleClose = () => {
  boxWidth.value = 0
  formWidth.value = 0
  emit('update:open', false)
  emit('on-close')
}

// 处理重置按钮
const handleReset = () => {
  emit('on-reset')
}

// 处理提交按钮
const handleSubmit = () => {
  emit('on-submit')
}
</script>

<script lang="ts">
export default {
  name: 'PopForm', // 修复拼写错误
}
</script>

<style scoped>
.pop-box {
  display: flex;
  justify-content: flex-end;
  position: fixed;
  top: 0;
  right: 0;
  height: 100vh;
  background: rgba(0, 0, 0, 0.25);
  z-index: 1000;
}

.pop-form {
  height: 100%;
  background: #fff;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease;
  box-shadow: -4px 0 8px rgba(147, 112, 219, 0.3);
  transform: translateX(0);
}

.pop-remark {
  border: dotted 1px rgba(147, 112, 219, 0.3);
  width: 100%;
  margin: 8px 0 24px 0;
  padding: 12px;
  font-size: 0.9em;
  color: #919191;
}

.pop-header {
  height: 56px;
  padding: 10px 16px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pop-body {
  flex: 1;
  overflow-y: auto; /* 内容超长时启用纵向滚动条 */
  padding: 16px;
}

.pop-footer {
  padding: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
