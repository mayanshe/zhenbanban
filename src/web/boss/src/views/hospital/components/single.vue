<template>
  <a-drawer :width="420" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose"
    unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-form-item field="hospitalName" label="医院名称 :">
          <a-input v-model="formData.hospitalName" placeholder="请输入医院名称" />
        </a-form-item>
        <a-form-item field="hospitalCode" label="医院编码 :">
          <a-input v-model="formData.hospitalCode" placeholder="请输入医院编码" />
        </a-form-item>
        <a-form-item field="usccCode" label="统一社会信用代码 :">
          <a-input v-model="formData.usccCode" placeholder="请输入统一社会信用代码" />
        </a-form-item>
        <a-form-item field="insuranceCode" label="医保编码 :">
          <a-input v-model="formData.insuranceCode" placeholder="请输入医保编码" />
        </a-form-item>
        <a-form-item field="ownershipType" label="所有制类型 :">
          <a-select v-model="formData.ownershipType" placeholder="请选择所有制类型">
            <a-option value="PUBLIC">公立</a-option>
            <a-option value="PRIVATE">私立</a-option>
            <a-option value="OTHER">其他</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="hospitalType" label="机构类型 :">
          <a-select v-model="formData.hospitalType" placeholder="请选择机构类型">
            <a-option value="GENERAL">综合医院</a-option>
            <a-option value="SPECIALTY">专科医院</a-option>
            <a-option value="TRADITIONAL">中医医院</a-option>
            <a-option value="ETHNIC">民族医医院</a-option>
            <a-option value="REHABILITATION">康复医院</a-option>
            <a-option value="OTHER">其他</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="hospitalLevel" label="机构等级 :">
          <a-select v-model="formData.hospitalLevel" placeholder="请选择机构等级">
            <a-option value="LEVEL-3A">三甲</a-option>
            <a-option value="LEVEL-3B">三乙</a-option>
            <a-option value="LEVEL-2A">二甲</a-option>
            <a-option value="LEVEL-2B">二乙</a-option>
            <a-option value="LEVEL-1">一级</a-option>
            <a-option value="OTHER">其他</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="status" label="状态 :">
          <a-select v-model="formData.status" placeholder="请选择状态">
            <a-option value="PENDING">待审核</a-option>
            <a-option value="ACTIVE">启用</a-option>
            <a-option value="INACTIVE">禁用</a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="provinceId" label="省份 :">
          <a-input-number v-model="formData.provinceId" placeholder="请输入省份ID" />
        </a-form-item>
        <a-form-item field="cityId" label="城市 :">
          <a-input-number v-model="formData.cityId" placeholder="请输入城市ID" />
        </a-form-item>
        <a-form-item field="countyId" label="区县 :">
          <a-input-number v-model="formData.countyId" placeholder="请输入区县ID" />
        </a-form-item>
        <a-form-item field="address" label="地址 :">
          <a-input v-model="formData.address" placeholder="请输入地址" />
        </a-form-item>
        <a-form-item field="postalCode" label="邮政编码 :">
          <a-input v-model="formData.postalCode" placeholder="请输入邮政编码" />
        </a-form-item>
        <a-form-item field="longitude" label="经度 :">
          <a-input-number v-model="formData.longitude" placeholder="请输入经度" />
        </a-form-item>
        <a-form-item field="latitude" label="纬度 :">
          <a-input-number v-model="formData.latitude" placeholder="请输入纬度" />
        </a-form-item>
        <a-form-item field="mapUrl" label="地图链接 :">
          <a-input v-model="formData.mapUrl" placeholder="请输入地图链接" />
        </a-form-item>
        <a-form-item field="contactPhone" label="联系电话 :">
          <a-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
        </a-form-item>
        <a-form-item field="contactEmail" label="联系邮箱 :">
          <a-input v-model="formData.contactEmail" placeholder="请输入联系邮箱" />
        </a-form-item>
        <a-form-item field="website" label="官网 :">
          <a-input v-model="formData.website" placeholder="请输入官网" />
        </a-form-item>
        <a-form-item field="companionDiagnosisEnabled" label="启用伴诊 :">
          <a-switch v-model="formData.companionDiagnosisEnabled" />
        </a-form-item>
        <a-form-item field="mealServiceEnabled" label="启用配餐 :">
          <a-switch v-model="formData.mealServiceEnabled" />
        </a-form-item>
        <a-form-item field="testingDeliveryEnabled" label="启用送检 :">
          <a-switch v-model="formData.testingDeliveryEnabled" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { FormInstance } from '@arco-design/web-vue/es/form';
import { Message } from '@arco-design/web-vue';
import { Hospital, getHospital, createHospital, updateHospital } from '@/api/hospital';

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
  title: '添加业务医院表',
  remark: '业务医院表用于后端接口控制。',
});

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val;
    if (val) {
      load();
    }
  }
);

const load = async () => {
  if (props.action === 'add') {
    formData.value = generate();
  } else {
    const resp = await getHospital(props.singleId);
    formData.value = (resp.data as Hospital) || generate();
  }
};

const generate = (): Hospital => {
  return {
    id: '0',
    ownershipType: '',
    hospitalType: '',
    hospitalLevel: '',
    status: '',
    insuranceCode: '',
    usccCode: '',
    hospitalCode: '',
    hospitalName: '',
    provinceId: 0,
    cityId: 0,
    countyId: 0,
    address: '',
    postalCode: '',
    longitude: 0,
    latitude: 0,
    mapUrl: '',
    contactPhone: '',
    contactEmail: '',
    website: '',
    companionDiagnosisEnabled: false,
    mealServiceEnabled: false,
    testingDeliveryEnabled: false,
  };
};

const formData = ref<Hospital>(generate());
const formRef = ref<FormInstance | null>(null);

const rules = {
  hospitalName: [{ required: true, message: '请输入医院名称' }],
  hospitalCode: [{ required: true, message: '请输入医院编码' }],
};

const handleValidate = async () => {
  const v = await new Promise((resolve) => {
    formRef.value?.validate((r) => {
      if (r === undefined) {
        resolve(true);
      } else {
        const firstError = Object.values(r)[0];
        Message.error(firstError.message);
        resolve(false);
      }
    });
  });
  return v;
};

const handleSubmit = async () => {
  if (props.action === 'add') {
    createHospital(formData.value).then(() => {
      Message.success(`创建成功`);
      handleClose();
      emit('on-success');
    });
  } else {
    updateHospital(formData.value).then(() => {
      Message.success('修改成功');
      handleClose();
      emit('on-success');
    });
  }
};

const handleClose = async () => {
  emit('update:open', false);
};
</script>

<script lang="ts">
export default {
  name: 'HospitalSingle',
};
</script>
