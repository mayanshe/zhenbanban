<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="pieceCode" label="饮片编码 :">
          <a-input v-model="formData.pieceCode" placeholder="请输入饮片编码" />
        </a-form-item>
        <a-form-item field="pieceName" label="饮片名称 :">
          <a-input v-model="formData.pieceName" placeholder="请输入饮片名称" />
        </a-form-item>
        <a-form-item field="type" label="类型 :">
          <a-select v-model="formData.type" placeholder="请选择类型">
            <a-option :value="1">十八反</a-option>
            <a-option :value="2">十九畏</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="antagonismPieceCodes" label="配伍编码 (逗号分隔) :">
          <a-textarea v-model="formData.antagonismPieceCodes" row="3" placeholder="请输入配伍的饮片编码，用逗号分隔" />
        </a-form-item>
        <a-form-item field="antagonismPieceNames" label="配伍名称 (逗号分隔) :">
          <a-textarea v-model="formData.antagonismPieceNames" row="3" placeholder="请输入配伍的饮片名称，用逗号分隔" />
        </a-form-item>
        <a-form-item field="remark" label="备注 :">
          <a-textarea v-model="formData.remark" row="3" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { Message } from '@arco-design/web-vue';
import { Antagonism, getAntagonism, createAntagonism, updateAntagonism } from '@/api/antagonism';

const props = defineProps<{
  open: boolean;
  action: string;
  singleId: string;
}>();

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void;
  (e: 'on-success'): void;
}>();

const dialog = ref({
  open: false,
  title: '添加',
});

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val;
    if (val) {
      dialog.value.title = props.action === 'add' ? '添加记录' : '修改记录';
      load();
    }
  }
);

const load = async () => {
  if (props.action === 'modify' && props.singleId) {
    const res = await getAntagonism(props.singleId);
    formData.value = res.data;
  } else {
    formData.value = generate();
  }
};

const generate = (): Antagonism => {
  return {
    id: '0',
    pieceCode: '',
    pieceName: '',
    antagonismPieceCodes: '',
    antagonismPieceNames: '',
    type: 1,
    remark: '',
  };
};

const formData = ref<Antagonism>(generate());
const formRef = ref<FormInstance | null>(null);

const rules = {
  pieceCode: [{ required: true, message: '请输入饮片编码' }],
  pieceName: [{ required: true, message: '请输入饮片名称' }],
  type: [{ required: true, message: '请选择类型' }],
  antagonismPieceCodes: [{ required: true, message: '请输入配伍编码' }],
  antagonismPieceNames: [{ required: true, message: '请输入配伍名称' }],
};

const handleValidate = async () => {
  const validation = await formRef.value?.validate();
  if (validation) {
    Message.error(Object.values(validation)[0].message);
    return false;
  }
  return true;
};

const handleSubmit = async () => {
  try {
    if (props.action === 'add') {
      await createAntagonism(formData.value);
      Message.success('创建成功');
    } else {
      await updateAntagonism(formData.value);
      Message.success('修改成功');
    }
    handleClose();
    emit('on-success');
  } catch (error) {
    // Error handling is managed by global axios interceptor
  }
};

const handleClose = () => {
  formRef.value?.resetFields();
  emit('update:open', false);
};
</script>

<script lang="ts">
export default {
  name: 'AntagonismSingle',
};
</script>
