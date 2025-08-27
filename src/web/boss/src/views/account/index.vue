<template>
  <div class="container">
    <Breadcrumb :items="['用户', '账号']" />
    <a-card class="general-card" title="账号管理">
      <a-divider style="margin-top: 0" />
      <a-divider style="margin-top: 0" />

      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button v-if="buttons.includes('account:add')" type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon>
                <icon-plus />
              </template>
              添加账号
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
          <a-tooltip content="刷新">
            <div class="action-icon"><icon-refresh size="18" /></div>
          </a-tooltip>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { Message } from '@arco-design/web-vue'

const route = useRoute()
const buttons = route.meta.buttons || []

// region 添加与编辑的交互
const single = ref({
  open: false,
  action: '',
  id: '0',
})

const handleOpenSingle = async (actionValue: string, idValue: string) => {
  single.value = {
    open: true,
    action: actionValue,
    id: idValue,
  }
}

const handleSuccess = async () => {
  console.log('采蘑菇的小姑娘')
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'AccountManage',
}
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

:deep(.arco-table-th) {
  &:last-child {
    .arco-table-th-item-title {
      margin-left: 16px;
    }
  }
}

.action-icon {
  margin-left: 12px;
  cursor: pointer;
}

.active {
  color: #0960bd;
  background-color: #e3f4fc;
}

.setting {
  display: flex;
  align-items: center;
  width: 200px;

  .title {
    margin-left: 12px;
    cursor: pointer;
  }
}
</style>
