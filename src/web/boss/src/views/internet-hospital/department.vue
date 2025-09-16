<template>
  <div class="container">
    <Breadcrumb :items="['互联网医院', '科室管理']" page-name="科室管理" route-name="DepartmentManage" />
    <a-card class="general-card" title="科室管理">
      <a-divider style="margin-top: 0" />
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="输入科室名称搜索" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="departmentType" label="科室类型:">
                  <a-select v-model="searchData.departmentType" placeholder="请选择科室类型" allow-clear>
                    <a-option value="clinical">临床科室</a-option>
                    <a-option value="technology">医技科室</a-option>
                    <a-option value="emergency">急诊与重症科室</a-option>
                    <a-option value="logistics">行政及后勤科室</a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 28px" direction="vertical" />
        <a-col :flex="'200px'" style="text-align: right">
          <a-space :size="18">
            <a-button @click="search" type="primary" size="small">
              <template #icon><icon-search /></template>
              搜索
            </a-button>
            <a-button @click="reset" size="small">
              <template #icon><icon-refresh /></template>
              重置
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-divider style="margin-top: 0" />
      <a-row style="margin-bottom: 24px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleOpenSingle('add', '0')">
              <template #icon><icon-plus /></template>
              添加科室
            </a-button>
          </a-space>
        </a-col>
      </a-row>

      <a-table
        style="margin-bottom: 16px"
        :columns="columns"
        :data="filteredDepartments"
        :pagination="false"
        row-key="id"
      >
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mini" @click="handleOpenSingle('modify', record.id)">编辑</a-button>
            <a-button type="text" size="mini" @click="handleOpenSingle('add', record.id)">添加子科室</a-button>
            <a-popconfirm content="确定删除此科室?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mini" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <DepartmentSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { Message } from '@arco-design/web-vue';
import { Department, DepartmentSearchModel, getAllDepartments, deleteDepartment } from '@/api/department';
import DepartmentSingle from './components/department-single.vue';

interface DepartmentTreeNode extends Department {
  children?: DepartmentTreeNode[];
}

// region 添加与编辑
const single = ref({
  open: false,
  action: 'add',
  id: '0',
});

const handleOpenSingle = (actionValue: string, idValue: string) => {
  single.value = {
    open: true,
    action: actionValue,
    id: idValue,
  };
};

const handleSuccess = () => {
  load();
};
// endregion

// region 列表
const columns = [
  { title: '科室名称', dataIndex: 'departmentName', width: 250 },
  { title: '科室类型', dataIndex: 'departmentType' },
  { title: '简介', dataIndex: 'summary' },
  { title: '创建时间', dataIndex: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt' },
  { title: '操作', slotName: 'optional', width: 200 },
];

const allDepartments = ref<DepartmentTreeNode[]>([]);
const searchData = ref<Partial<DepartmentSearchModel>>({ keywords: '', departmentType: '' });

const listToTree = (items: Department[], parentId = '0'): DepartmentTreeNode[] => {
  return items
    .filter((item) => item.parentId === parentId)
    .map((item) => {
      const children = listToTree(items, item.id);
      const node: DepartmentTreeNode = { ...item };
      if (children.length > 0) {
        node.children = children;
      }
      return node;
    });
};

const load = async () => {
  try {
    const res = await getAllDepartments();
    allDepartments.value = listToTree(res.data || []);
  } catch (error) {
    Message.error('加载科室列表失败');
  }
};

const search = () => {
  // The filtering logic is now in the computed property 'filteredDepartments'
  // This function is just to trigger reactivity if needed, or can be left empty
  // if direct v-model binding on search inputs is sufficient.
};

const reset = () => {
  searchData.value = { keywords: '', departmentType: '' };
};

const filterTree = (tree: DepartmentTreeNode[], keyword: string, type: string): DepartmentTreeNode[] => {
  const result: DepartmentTreeNode[] = [];
  if (!tree) {
    return result;
  }

  for (const node of tree) {
    const keywordMatch = keyword ? node.departmentName.toLowerCase().includes(keyword.toLowerCase()) : true;
    const typeMatch = type ? node.departmentType === type : true;

    const children = node.children ? filterTree(node.children, keyword, type) : [];

    if ((keywordMatch && typeMatch) || children.length > 0) {
      const newNode = { ...node, children };
      result.push(newNode);
    }
  }

  return result;
}


const filteredDepartments = computed(() => {
  const { keywords = '', departmentType = '' } = searchData.value;
  return filterTree(allDepartments.value, keywords, departmentType)
});


const handleDelete = async (id: string) => {
  try {
    await deleteDepartment(id);
    Message.success('删除成功');
    load();
  } catch (error) {
    Message.error('删除失败');
  }
};

load();
// endregion
</script>

<script lang="ts">
export default {
  name: 'DepartmentManage',
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
