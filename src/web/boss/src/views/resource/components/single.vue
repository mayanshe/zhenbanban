<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-alert :show-icon="true" class="remark">{{ dialog.remark }}</a-alert>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="parentId" label="父资源 : ">
          <a-cascader v-model="formData.parentId" :options="parents" placeholder="请选择" allow-clear check-strictly />
        </a-form-item>
        <a-form-item field="resourceType" label="资源类型 : ">
          <a-select v-model="formData.resourceType" placeholder="请选择">
            <a-option v-for="item of resourceTypes" :key="item.code" :value="item.code" :label="item.name" />
          </a-select>
        </a-form-item>
        <a-form-item field="resourceName" label="资源名称 : ">
          <a-input v-model="formData.resourceName" placeholder="字母及-及:" />
        </a-form-item>
        <a-form-item field="displayName" label="资源显示名称 : ">
          <a-input v-model="formData.displayName" placeholder="汉字" />
        </a-form-item>
        <a-form-item field="path" label="路径 : " v-if="formData.resourceType === 'menu' || formData.resourceType === 'component'">
          <a-input v-model="formData.path" />
        </a-form-item>
        <a-form-item field="component" label="组件 : " v-if="formData.resourceType === 'component'">
          <a-input v-model="formData.component" />
        </a-form-item>
        <a-form-item field="url" label="链接 : " v-if="formData.resourceType === 'url'">
          <a-input v-model="formData.url" />
        </a-form-item>
        <a-form-item field="icon" label="图标 : ">
          <a-input v-model="formData.icon" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" row="3" />
        </a-form-item>
        <a-form-item field="sort" label="排序（按倒序） : ">
          <a-input v-model="formData.sort" placeholder="倒序" />
        </a-form-item>
        <a-form-item
          field="showInMenu"
          label="是否显示在菜单中 : "
          v-if="formData.resourceType !== '' && formData.resourceType !== 'button'"
        >
          <a-radio-group v-model="formData.showInMenu">
            <a-radio :value="true">是</a-radio>
            <a-radio :value="false">否</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, unref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { CascaderOption } from '@/api/common'
import {
  createResource,
  getResource,
  getResourceTypeList,
  getResourceList,
  Resource,
  ResourceType,
  updateResource,
  ResourceView,
} from '@/api/resource'

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
  title: '添加资源',
  remark: '包含菜单及页面按钮。仅支持三级。',
})

watch(
  () => props.open,
  (obj) => {
    dialog.value.open = props.open
    dialog.value.title = props.action === 'add' ? '添加资源' : '修改资源'
    loadResourceType()
    load()
    loadParents()
  }
)

const parents = ref<CascaderOption[]>([
  {
    value: '0',
    label: '根',
  },
])

const transParent = (data: ResourceView[]) => {
  if (data.length === 0) {
    return []
  }

  const lists = []
  for (let i = 0; i < data.length; i += 1) {
    const item = data[i]
    if (item.resourceType.code.toLowerCase() !== 'button') {
      const childs = transParent(item.children)
      if (childs.length > 0) {
        lists.push({
          value: item.id,
          label: item.displayName,
          children: childs,
        })
      } else {
        lists.push({
          value: item.id,
          label: item.displayName,
        })
      }
    }
  }

  return lists
}

const loadParents = async () => {
  getResourceList()
    .then((res) => {
      parents.value = [{ value: '0', label: '根' }, ...transParent(res)]
      console.log(JSON.stringify(parents.value))
    })
    .catch((err) => {
      parents.value = [
        {
          value: '0',
          label: '根',
        },
      ]
    })
}

// region 加载所有资源类型
const resourceTypes = ref<ResourceType[]>([])

const loadResourceType = async () => {
  resourceTypes.value = (await getResourceTypeList()) || []
}
// endregion

// region 表单定义
const generate = () => {
  return {
    id: '',
    parentId: '0',
    resourceType: '',
    resourceName: '',
    displayName: '',
    description: '',
    icon: '',
    url: '',
    path: '',
    component: '',
    sort: '0',
    showInMenu: false,
  }
}

const rules = {
  parentId: [{ required: true, message: '请选择父资源' }],
  resourceType: [{ required: true, message: '请选择资源类型' }],
  resourceName: [
    { required: true, message: '请输入资源名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
    { match: /^[a-zA-Z-\\:]+$/g, message: '只能由字母 - : 组成' },
  ],
  displayName: [
    { required: true, message: '请输入资源显示名称' },
    { maxLength: 45, message: '不能超过最大长度45个字符' },
  ],
  path: [
    {
      validator: (value, callback) => {
        if ((formData.value.resourceType === 'menu' || formData.value.resourceType === 'component') && !value) {
          callback('请输入资源路径')
        } else {
          callback()
        }
      },
    },
    { maxLength: 255, message: '不能超过最大长度255个字符' },
  ],
  component: [
    {
      validator: (value, callback) => {
        if (formData.value.resourceType === 'component' && !value) {
          callback('请输入资源组件路径')
        } else {
          callback()
        }
      },
    },
    { maxLength: 255, message: '不能超过最大长度255个字符' },
  ],
  url: [
    {
      validator: (value, callback) => {
        if (formData.value.resourceType === 'link' && !value) {
          callback('请输入资源URl')
        } else {
          callback()
        }
      },
    },
    { maxLength: 255, message: '不能超过最大长度255个字符' },
  ],
  description: [{ maxLength: 255, message: '不能超过最大长度255个字符' }],
  sort: [
    { required: true, message: '请输入资源排序' },
    { match: /^[0-9]\d*$/, message: '排序只能是非负整数' },
  ],
}

const formData = ref<Resource>(generate())
const formRef = ref<FormInstance | null>(null)

const load = () => {
  if (props.action === 'add') {
    formData.value = generate()
  } else {
    getResource(props.singleId)
      .then((res: ResourceView) => {
        formData.value = {
          id: res.id,
          parentId: res.parentId,
          resourceType: res.resourceType.code,
          resourceName: res.resourceName,
          displayName: res.displayName,
          description: res.description,
          icon: res.icon,
          url: res.url,
          path: res.path,
          component: res.component,
          sort: res.sort,
          showInMenu: res.showInMenu,
        }
      })
      .catch((err) => {
        formData.value = generate()
        handleClose()
      })
  }
}
// endregion

// region 表单事件
// 关闭
const handleClose = () => {
  emit('update:open', false)
}

// 验证
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

// 提交
const handleSubmit = async () => {
  if (props.action === 'add') {
    createResource(unref(formData)).then(() => {
      Message.success('添加资源成功')
      handleClose()
      emit('on-success')
    })
  } else {
    updateResource(unref(formData)).then(() => {
      Message.success('修改资源信息成功')
      handleClose()
      emit('on-success')
    })
  }
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'ResourceSingle',
}
</script>

<style scoped lang="less">
.remark {
  margin: 10px 0 20px 0;
}
</style>
