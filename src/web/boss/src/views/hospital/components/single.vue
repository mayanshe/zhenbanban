<template>
  <a-drawer :width="600" :visible="dialog.open" @before-ok="handleValidate" @ok="handleSubmit" @cancel="handleClose" unmountOnClose>
    <template #title>
      {{ dialog.title }}
    </template>
    <div>
      <a-form layout="vertical" :model="formData" :rules="rules" ref="formRef">
        <a-row :gutter="16">
          <a-col :span="5">
            <a-form-item field="companionDiagnosisEnabled" label="启用伴诊 :">
              <a-switch v-model="formData.companionDiagnosisEnabled" />
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item field="mealServiceEnabled" label="启用配餐 :">
              <a-switch v-model="formData.mealServiceEnabled" />
            </a-form-item>
          </a-col>
        </a-row>

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
        <a-form-item field="ownershipType" label="医院所有制类型 :">
          <a-select v-model="formData.ownershipType" placeholder="请选择医院所有制类型">
            <a-option v-for="item in hospitalOwnershipTypes" :key="item.code" :value="item.code">
              {{ item.name }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="hospitalType" label="医院类型 :">
          <a-select v-model="formData.hospitalType" placeholder="请选择医院类型">
            <a-option v-for="item in hospitalTypes" :key="item.code" :value="item.code">
              {{ item.name }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item field="hospitalLevel" label="医院等级 :">
          <a-select v-model="formData.hospitalLevel" placeholder="请选择医院等级">
            <a-option v-for="item in hospitalLevels" :key="item.code" :value="item.code">
              {{ item.name }}
            </a-option>
          </a-select>
        </a-form-item>

        <a-form-item field="provinceId" label="所在省/直辖市 : " :span="6">
          <a-select v-model="formData.provinceId" placeholder="请选择" @change="loadCity(true)">
            <a-option style="width: 50%" v-for="item in provinces" :value="item.id">{{ item.regionName }}</a-option>
          </a-select>
        </a-form-item>

        <a-form-item field="cityId" label="所在市 : " :span="6">
          <a-select v-model="formData.cityId" placeholder="请选择" @change="loadCounty(true)">
            <a-option style="width: 50%" v-for="item in cities" :value="item.id">{{ item.regionName }}</a-option>
          </a-select>
        </a-form-item>

        <a-form-item field="countyId" label="所在区县 : " :span="6">
          <a-select v-model="formData.countyId" placeholder="请选择">
            <a-option style="width: 50%" v-for="item in counties" :value="item.id">{{ item.regionName }}</a-option>
          </a-select>
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
        <a-form-item field="summary" label="医院简介 :">
          <a-textarea v-model="formData.summary" placeholder="请输入医院简介"  :row="`3`"/>
        </a-form-item>
        <a-form-item field="description" label="医院详情 :">
          <a-textarea v-model="formData.description" placeholder="请输入医院详情" :row="`5`"/>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { ValueObject } from '@/api/common'
import {
  Hospital,
  getHospitalLevelList,
  getHospitalOwnershipTypeList,
  getHospitalTypeList,
  getHospital,
  createHospital,
  updateHospital,
} from '@/api/hospital'
import { getRegionList, RegionView } from '@/api/region'

const props = defineProps<{
  open: boolean
  action: string
  singleId: string
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'on-success'): void
}>()

const dialog = ref({
  open: false,
  title: '添加业务医院',
  remark: '业务医院用于后端接口控制。',
})

// region 医院数据加载
watch(
  () => props.open,
  (val) => {
    dialog.value.open = val
    if (val) {
      load()
    }
  }
)

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
    provinceId: '',
    province: '',
    cityId: '',
    city: '',
    countyId: '',
    county: '',
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
  }
}

const load = async () => {
  if (props.action === 'add') {
    formData.value = generate()
  } else {
    const resp = (await getHospital(props.singleId)) || generate()
    formData.value = {
      id: resp.id,
      ownershipType: resp.ownershipType.code,
      hospitalType: resp.hospitalType.code,
      hospitalLevel: resp.hospitalLevel.code,
      status: resp.status.code,
      insuranceCode: resp.insuranceCode,
      usccCode: resp.usccCode,
      hospitalCode: resp.hospitalCode,
      hospitalName: resp.hospitalName,
      provinceId: resp.provinceId,
      province: resp.province,
      cityId: resp.cityId,
      city: resp.city,
      countyId: resp.countyId,
      county: resp.county,
      address: resp.address,
      postalCode: resp.postalCode,
      longitude: resp.longitude,
      latitude: resp.latitude,
      mapUrl: resp.mapUrl,
      contactPhone: resp.contactPhone,
      contactEmail: resp.contactEmail,
      website: resp.website,
      summary: resp.summary,
      description: resp.description,
      companionDiagnosisEnabled: resp.companionDiagnosisEnabled,
      mealServiceEnabled: resp.mealServiceEnabled,
      testingDeliveryEnabled: resp.testingDeliveryEnabled,
    }
  }
  loadProvince()
  loadCity(false)
  loadCounty(false)
}

const formData = ref<Hospital>(generate())
const formRef = ref<FormInstance | null>(null)

const rules = {
  hospitalName: [{ required: true, message: '请输入医院名称' }],
  hospitalCode: [{ required: true, message: '请输入医院编码' }],
  ownershipType: [{ required: true, message: '请选择医院所有制类型' }],
  hospitalType: [{ required: true, message: '请选择医院类型' }],
  hospitalLevel: [{ required: true, message: '请选择医院等级' }],
  provinceId: [{ required: true, message: '请选择省/直辖市' }],
  cityId: [{ required: true, message: '请选择市' }],
  countyId: [{ required: true, message: '请选择区县' }],
  address: [{ required: true, message: '请输入地址' }],
  postalCode: [{ required: true, message: '请输入邮政编码' }],
  longitude: [{ required: true, message: '请输入经度' }],
  latitude: [{ required: true, message: '请输入纬度' }],
}

const handleValidate = async () => {
  const v = await new Promise((resolve) => {
    formRef.value?.validate((r) => {
      if (r === undefined) {
        resolve(true)
      } else {
        const firstError = Object.values(r)[0]
        Message.error(firstError.message)
        resolve(false)
      }
    })
  })
  return v
}

// ednregion

// region 获取医院键值对属性
const hospitalLevels = ref<ValueObject[]>([])
const loadHospitalLevels = async () => {
  const resp = await getHospitalLevelList()
  hospitalLevels.value = resp || []
}
loadHospitalLevels()

const hospitalOwnershipTypes = ref<ValueObject[]>([])
const loadHospitalOwnershipTypes = async () => {
  const resp = await getHospitalOwnershipTypeList()
  hospitalOwnershipTypes.value = resp || []
}
loadHospitalOwnershipTypes()

const hospitalTypes = ref<ValueObject[]>([])
const loadHospitalTypes = async () => {
  const resp = await getHospitalTypeList()
  hospitalTypes.value = resp || []
}
loadHospitalTypes()
// endregion

// region 加载行政区划数据
const provinces = ref<RegionView[]>([])
const cities = ref<RegionView[]>([])
const counties = ref<RegionView[]>([])
const loadProvince = async () => {
  provinces.value = (await getRegionList('0')) || []
}
const loadCity = async (reselect: boolean) => {
  cities.value = (await getRegionList(formData.value.provinceId)) || []
  if (reselect) {
    counties.value = []
    formData.value.cityId = ''
    formData.value.city = ''
    formData.value.countyId = ''
    formData.value.county = ''
  }
}
const loadCounty = async (reselect: boolean) => {
  counties.value = (await getRegionList(formData.value.cityId)) || []
  if (reselect) {
    formData.value.countyId = ''
    formData.value.county = ''
  }
}
// endregion

const handleSubmit = async () => {
  if (props.action === 'add') {
    createHospital(formData.value).then(() => {
      Message.success(`创建成功`)
      handleClose()
      emit('on-success')
    })
  } else {
    updateHospital(formData.value).then(() => {
      Message.success('修改成功')
      handleClose()
      emit('on-success')
    })
  }
}

const handleClose = async () => {
  emit('update:open', false)
}
</script>

<script lang="ts">
export default {
  name: 'HospitalSingle',
}
</script>
