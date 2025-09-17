<template>
    <div class="container">
        <Breadcrumb :items="['业务', '医院管理']" page-name="业务医院表" route-name="HospitalManage" />
        <a-card class="general-card" title="业务医院表管理">
            <a-divider style="margin-top: 0" />

            <a-row>
                <a-col :flex="1">
                    <a-form :model="searchData" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                        label-align="left">
                        <a-row :gutter="16">
                            <a-col :span="8">
                                <a-form-item field="keywords" label="关键词:">
                                    <a-input v-model="searchData.keywords" placeholder="医院名或编码" />
                                </a-form-item>
                            </a-col>
                            <a-col :span="8">
                                <a-form-item field="hospitalCode" label="医院编码:">
                                    <a-input v-model="searchData.hospitalCode" placeholder="请输入医院编码" />
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
                            添加业务医院表
                        </a-button>
                    </a-space>
                </a-col>
                <a-col :span="12" style="display: flex; align-items: center; justify-content: end; padding-top: 12px">
                    <a-tooltip content="刷新">
                        <div class="action-icon" @click="search">
                            <icon-refresh size="18" />
                        </div>
                    </a-tooltip>
                </a-col>
            </a-row>

            <a-table style="margin-bottom: 16px" :columns="columns" :data="hospitals.items" :pagination="pagination"
                @page-change="handlePageChange">
                <template #optional="{ record }">
                    <a-space>
                        <a-button type="text" size="mimi" @click="handleOpenSingle('modify', record.id)">
                            编辑
                        </a-button>
                        <a-popconfirm content="确定删除此业务医院表?" @ok="handleDelete(record.id)">
                            <a-button type="text" size="mimi" status="danger">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-card>

        <HospitalSingle v-model:open="single.open" :action="single.action" :single-id="single.id"
            @on-success="handleSuccess" />
    </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { Message } from '@arco-design/web-vue';
import { Pager, Pagination } from '@/api/common';
import { HospitalView, HospitalSearchModel, getHospitalPagination, deleteHospital } from '@/api/hospital';
import HospitalSingle from './components/single.vue';

const route = useRoute();
const buttons = route.meta.buttons || [];

const single = ref({
    open: false,
    action: '',
    id: '0',
});

const handleOpenSingle = async (actionValue: string, idValue: string) => {
    single.value = {
        open: true,
        action: actionValue,
        id: idValue,
    };
};

const handleSuccess = async () => {
    load();
};

const columns = [
    { title: '医院名称', dataIndex: 'hospitalName' },
    { title: '医院编码', dataIndex: 'hospitalCode' },
    { title: '所有制', dataIndex: 'ownershipType' },
    { title: '类型', dataIndex: 'hospitalType' },
    { title: '等级', dataIndex: 'hospitalLevel' },
    { title: '状态', dataIndex: 'status' },
    { title: '创建时间', dataIndex: 'createdAt' },
    { title: '操作', slotName: 'optional' },
];

const pager = ref<Pager>({
    page: 1,
    pageSize: 10,
});

const generateSearchModel = (): HospitalSearchModel => {
    return {
        keywords: '',
        hospitalCode: '',
        deleted: false,
    };
};

const reset = () => {
    searchData.value = generateSearchModel();
};

const generateDatalist = (): Pagination<HospitalView> => {
    return {
        page: 1,
        pageSize: 10,
        count: 0,
        total: 0,
        totalPage: 0,
        prevPage: 1,
        nextPage: 1,
        items: [],
    };
};

const searchData = ref<HospitalSearchModel>(generateSearchModel());
const hospitals = ref<Pagination<HospitalView>>(generateDatalist());

const load = async () => {
    const response = await getHospitalPagination(searchData.value, pager.value);
    hospitals.value = response.data || generateDatalist();
};

const search = async () => {
    pager.value.page = 1;
    load();
};

load();

const pagination = computed(() => {
    return {
        total: hospitals.value.total,
        pageSize: hospitals.value.pageSize,
        current: hospitals.value.page,
    };
});

const handlePageChange = (page: number) => {
    pager.value.page = page;
    load();
};

const handleDelete = async (id: string) => {
    deleteHospital(id).then(() => {
        Message.success('删除成功');
        search();
    });
};
</script>

<script lang="ts">
export default {
    name: 'HospitalManage',
};
</script>

<style scoped lang="less">
.container {
    padding: 0 20px 20px 20px;
}

.action-icon {
    margin-left: 12px;
    cursor: pointer;
}
</style>
