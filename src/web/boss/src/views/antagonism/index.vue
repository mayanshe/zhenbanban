<template>
  <div class="container">
    <Breadcrumb :items="['字典', '十八反十九畏']" />
    <a-card class="general-card" title="十八反十九畏管理">
      <a-divider style="margin-top: 0" />
      <a-row>
        <a-col :flex="1">
          <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }" label-align="left">
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="keywords" label="关键词:">
                  <a-input v-model="searchData.keywords" placeholder="饮片编码或名称" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="type" label="类型:">
                  <a-select v-model="searchData.type" placeholder="请选择类型" allow-clear>
                    <a-option :value="1">十八反</a-option>
                    <a-option :value="2">十九畏</a-option>
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
              <template #icon>
                <icon-search />
              </template>
              搜索
            </a-button>
            <a-button @click="reset" size="small">
              <template #icon>
                <icon-refresh />
              </template>
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
              <template #icon>
                <icon-plus />
              </template>
              添加记录
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-table
        :columns="columns"
        :data="antagonisms.items"
        :pagination="pagination"
        @page-change="handlePageChange"
      >
        <template #type="{ record }">
          <a-tag :color="record.type === 1 ? 'red' : 'orange'">
            {{ record.type === 1 ? '十八反' : '十九畏' }}
          </a-tag>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button type="text" size="mini" @click="handleOpenSingle('modify', record.id)">
              编辑
            </a-button>
            <a-popconfirm content="确定删除此记录?" @ok="handleDelete(record.id)">
              <a-button type="text" size="mini" status="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <AntagonismSingle v-model:open="single.open" :action="single.action" :single-id="single.id" @on-success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { Message } from '@arco-design/web-vue';
import { Pager, Pagination } from '@/api/common';
import { AntagonismView, AntagonismSearchModel, getAntagonismPagination, deleteAntagonism } from '@/api/antagonism';
import AntagonismSingle from './components/single.vue';

const single = ref({
  open: false,
  action: 'add',
  id: '0',
});

const handleOpenSingle = (action: string, id: string) => {
  single.value = {
    open: true,
    action,
    id,
  };
};

const handleSuccess = () => {
  load();
};

const columns = [
  { title: 'ID', dataIndex: 'id' },
  { title: '饮片编码', dataIndex: 'pieceCode' },
  { title: '饮片名称', dataIndex: 'pieceName' },
  { title: '类型', slotName: 'type' },
  { title: '配伍名称', dataIndex: 'antagonismPieceNames' },
  { title: '创建时间', dataIndex: 'gmtCreated' },
  { title: '更新时间', dataIndex: 'gmtModified' },
  { title: '操作', slotName: 'optional' },
];

const pager = ref<Pager>({
  page: 1,
  pageSize: 15,
});

const generateSearchModel = (): AntagonismSearchModel => ({
  keywords: '',
  type: undefined,
});

const searchData = ref<AntagonismSearchModel>(generateSearchModel());

const reset = () => {
  searchData.value = generateSearchModel();
  search();
};

const generateDataList = (): Pagination<AntagonismView> => ({
  page: 1,
  pageSize: 15,
  count: 0,
  total: 0,
  totalPage: 0,
  prevPage: 1,
  nextPage: 1,
  items: [],
});

const antagonisms = ref<Pagination<AntagonismView>>(generateDataList());

const load = async () => {
  const res = await getAntagonismPagination(searchData.value, pager.value);
  antagonisms.value = res.data || generateDataList();
};

const search = () => {
  pager.value.page = 1;
  load();
};

load();

const pagination = computed(() => ({
  total: antagonisms.value.total,
  pageSize: antagonisms.value.pageSize,
  current: antagonisms.value.page,
}));

const handlePageChange = (page: number) => {
  pager.value.page = page;
  load();
};

const handleDelete = async (id: string) => {
  try {
    await deleteAntagonism(id);
    Message.success('删除成功');
    search();
  } catch (error) {
    // Error handling
  }
};
</script>

<script lang="ts">
export default {
  name: 'AntagonismList',
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
