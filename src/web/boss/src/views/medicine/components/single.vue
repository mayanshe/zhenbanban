<template>
  <a-drawer :width="520" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="medicineCode" label="药品编码 : ">
              <a-input v-model="formData.medicineCode" placeholder="请输入药品编码" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="medicineName" label="药品商品名称 : ">
              <a-input v-model="formData.medicineName" placeholder="请输入药品商品名称" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="registeredName" label="药品注册名称 : ">
              <a-input v-model="formData.registeredName" placeholder="请输入药品注册名称" />
            </a-form-item>
          </a-col>
           <a-col :span="12">
             <a-form-item field="companyName" label="生产企业 : ">
               <a-input v-model="formData.companyName" placeholder="请输入生产企业" />
             </a-form-item>
           </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="registeredMedicineModel" label="注册剂型 : ">
              <a-input v-model="formData.registeredMedicineModel" placeholder="请输入注册剂型" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="realityMedicineModel" label="实际剂型 : ">
              <a-input v-model="formData.realityMedicineModel" placeholder="请输入实际剂型" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="registeredOutlook" label="注册规格 : ">
              <a-input v-model="formData.registeredOutlook" placeholder="请输入注册规格" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="realityOutlook" label="实际规格 : ">
              <a-input v-model="formData.realityOutlook" placeholder="请输入实际规格" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="approvalCode" label="批准文号 : ">
              <a-input v-model="formData.approvalCode" placeholder="请输入批准文号" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="standardCode" label="本位码 : ">
              <a-input v-model="formData.standardCode" placeholder="请输入本位码" />
            </a-form-item>
          </a-col>
        </a-row>
         <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="unit" label="最小包装单位 : ">
              <a-input v-model="formData.unit" placeholder="请输入最小包装单位" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item field="minUnit" label="最小制剂单位 : ">
              <a-input v-model="formData.minUnit" placeholder="请输入最小制剂单位" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item field="materialName" label="包装材质 : ">
              <a-input v-model="formData.materialName" placeholder="请输入包装材质" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
             <a-form-item field="factor" label="最小包装数量 : ">
               <a-input-number v-model="formData.factor" placeholder="请输入最小包装数量" />
             </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
            <a-col :span="12">
                <a-form-item field="otc" label="OTC?">
                  <a-radio-group v-model="formData.otc" type="button">
                    <a-radio :value="0">否</a-radio>
                    <a-radio :value="1">是</a-radio>
                  </a-radio-group>
                </a-form-item>
            </a-col>
            <a-col :span="12">
                <a-form-item field="poisonous" label="毒麻?">
                  <a-radio-group v-model="formData.poisonous" type="button">
                    <a-radio :value="0">否</a-radio>
                    <a-radio :value="1">是</a-radio>
                  </a-radio-group>
                </a-form-item>
            </a-col>
        </a-row>
        <a-form-item field="indication" label="适应症 : ">
          <a-textarea v-model="formData.indication" :rows="3" placeholder="请输入适应症" />
        </a-form-item>
        <a-form-item field="description" label="描述 : ">
          <a-textarea v-model="formData.description" :rows="3" placeholder="请输入描述" />
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { Medicine, getMedicine, createMedicine, updateMedicine } from '@/api/medicine'

// 定义组件props
const props = defineProps<{
  open: boolean // 是否打开
  action: string // 行为 add or modify
  singleId: string // 药品Id
}>()

// 定义 emits
const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加药品'
})

watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      if (props.action === 'modify') {
        dialog.value.title = '修改药品'
        load()
      } else {
        dialog.value.title = '添加药品'
        formData.value = generate()
      }
    }
  }
)

// region 加载
const load = async () => {
  formData.value = (await getMedicine(props.singleId)) || generate()
}
// endregion

// region 表单定义
const generate = (): Medicine => {
  return {
    id: '0',
    medicineCode: '',
    medicineName: '',
    registeredName: '',
    registeredMedicineModel: '',
    realityMedicineModel: '',
    registeredOutlook: '',
    realityOutlook: '',
    materialName: '',
    factor: 0,
    unit: '',
    minUnit: '',
    companyName: '',
    approvalCode: '',
    standardCode: '',
    indication: '',
    description: '',
    otc: 0,
    poisonous: 0
  }
}

const formData = ref<Medicine>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  medicineCode: [{ required: true, message: '请输入药品编码' }],
  medicineName: [{ required: true, message: '请输入药品商品名称' }],
  registeredName: [{ required: true, message: '请输入药品注册名称' }]
}
// endregion

// region 事件相应
// 表单验证
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

// 表单提交
const handleSubmit = async () => {
  if (props.action === 'add') {
    createMedicine(formData.value).then(() => {
      Message.success(`创建药品成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateMedicine(formData.value).then(() => {
      Message.success('修改药品信息成功')
      handleClose()
      emit('on-success')
    })
  }
}

// 关闭
const handleClose = async () => {
  generate()
  emit('update:open', false)
}
// endregion
</script>

<script lang="ts">
export default {
  name: 'MedicineSingle'
}
</script>
