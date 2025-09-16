<template>
  <div class="guide-container">
    <a-breadcrumb class="container-breadcrumb">
      <a-breadcrumb-item>
        <icon-apps />
      </a-breadcrumb-item>
      <a-breadcrumb-item v-for="item in items" :key="item">
        {{ item }}
      </a-breadcrumb-item>
    </a-breadcrumb>
    <a-link @click="handleAddQuickMenu" v-if="pageName != '' && routeName != ''" title="加入快捷菜单">
      <icon-plus-circle-fill style="font-size: 18px" />
    </a-link>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue'
import { useAppStore } from '@/store'
import { addQuickMenu } from '@/api/quick-menu'
import { Message } from '@arco-design/web-vue'

const appStore = useAppStore()

const props = defineProps({
  items: {
    type: Array as PropType<string[]>,
    default() {
      return []
    },
  },
  pageName: {
    type: String,
    default: '',
  },
  routeName: {
    type: String,
    default: '',
  },
})

const handleAddQuickMenu = async () => {
  const data = {
    pageName: props.pageName,
    routeName: props.routeName,
  }
  addQuickMenu(data).then(() => {
    Message.success('添加成功')
    appStore.updateSettings({ quickMenus: appStore.quickMenus + 1 })
  })
}
</script>

<style scoped lang="less">
.guide-container {
  display: flex;
  justify-content: space-between;
  border: none;
  padding: 0;
  margin: -4px 0;
}
.container-breadcrumb {
  margin: 16px 0;
  :deep(.arco-breadcrumb-item) {
    color: rgb(var(--gray-6));
    &:last-child {
      color: rgb(var(--gray-8));
    }
  }
}
</style>
