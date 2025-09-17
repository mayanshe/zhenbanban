<template>
  <div class="container">
    <Breadcrumb :items="['互联网医院', '互医配置']" page-name="互医配置" route-name="InternetHospitalSetting" />
    <a-card class="general-card" title="互联网医院配置">
      <a-form layout="horizontal" :model="formData" :rules="rules" ref="formRef">
        <a-row :gutter="72">
          <a-col :span="12">
            <a-form-item field="hospitalName" label="互联网医院名称 : " :span="6">
              <a-input v-model="formData.hospitalName" placeholder="请输入互联网医院名称" />
            </a-form-item>

            <a-form-item field="licenseNumber" label="医疗许可证号 : " :span="6">
              <a-input v-model="formData.licenseNumber" placeholder="请输入医疗许可证号" />
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

            <a-form-item field="address" label="详细地址 : " :span="6">
              <a-input v-model="formData.address" placeholder="请输入详细地址" />
            </a-form-item>

            <a-form-item field="email" label="联系邮箱 : " :span="6">
              <a-input v-model="formData.email" placeholder="请输入联系邮箱" />
            </a-form-item>

            <a-form-item field="contactNumbers" label="联系电话 : " :span="6">
              <a-input v-model="formData.contactNumbers" placeholder="请输入联系电话，多个请用英文,号分隔" />
            </a-form-item>

            <a-form-item field="serviceTimes" label="接诊时间 : " :span="6">
              <a-input v-model="formData.serviceTimes" placeholder="请输入接诊时间，多个请用英文,号分隔" />
            </a-form-item>

            <a-form-item field="website" label="医院网址链接 : " :span="6">
              <a-input v-model="formData.website" placeholder="请输入website，不要带http://或https://" />
            </a-form-item>

            <a-form-item field="introduction" label="互联网医院简介 : " :span="6">
              <a-textarea v-model="formData.introduction" placeholder="请输入互联网医院简介" auto-size />
            </a-form-item>
          </a-col>
          <a-col :span="12" style="border-left: dotted 1px #ccc">当前仅提供必要的互联网医院配置，其他配置请自行添加。</a-col>
        </a-row>
        <a-row>
          <a-col :span="24" style="text-align: center; margin: 24px 0">
            <a-button @click="handleSubmit" v-if="buttons.includes('internet-hospital-setting:modify')" status="success">
              修改互联网医院配置
            </a-button>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, unref } from 'vue'
import { useRoute } from 'vue-router'
import { FormInstance } from '@arco-design/web-vue/es/form'
import { Message } from '@arco-design/web-vue'
import { InternetHospitalSetting, getInternetHospitalSetting, modifyInternetHospitalSetting } from '@/api/option'
import { RegionView, getRegionList } from '@/api/region'

const route = useRoute()
const buttons = route.meta.buttons || []

const generateData = () => {
  return {
    hospitalName: '',
    licenseNumber: '',
    provinceId: '',
    province: '',
    cityId: '',
    city: '',
    countyId: '',
    county: '',
    address: '',
    contactNumbers: '',
    serviceTimes: '',
    website: '',
    email: '',
    introduction: '',
  }
}

const formData = ref<InternetHospitalSetting>(generateData())
const formRef = ref<FormInstance | null>(null)
const rules = {
  hospitalName: { required: true, message: '请输入互练完那个医院名称' },
  licenseNumber: { required: true, message: '请输入医疗许可证号' },
  provinceId: { required: true, message: '请选择省' },
  cityId: { required: true, message: '请选择市' },
  countyId: { required: true, message: '请选择区县' },
  address: { required: true, message: '请填写详细地址' },
  email: { required: true, message: '请填写联系邮箱' },
  contactNumbers: { required: true, message: '请填写联系电话' },
  serviceTimes: { required: true, message: '请填写接诊时间' },
  website: { required: true, message: '请填写医院网址' },
  introduction: { required: true, message: '请填写医院简介' },
}

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

const load = async () => {
  const view = (await getInternetHospitalSetting()) || generateData()
  formData.value = {
    hospitalName: view.hospitalName,
    licenseNumber: view.licenseNumber,
    provinceId: view.provinceId,
    province: view.province,
    cityId: view.cityId,
    city: view.cityId,
    countyId: view.countyId,
    county: view.county,
    address: view.address,
    contactNumbers: view.contactNumbers.join(','),
    serviceTimes: view.serviceTimes.join(','),
    website: view.website,
    email: view.email,
    introduction: view.introduction,
  }
  loadProvince()
  loadCity(false)
  loadCounty(false)
}
load()

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

const handleSubmit = async () => {
  handleValidate().then(() => {
    formData.value.province = provinces.value.find((item) => item.id === formData.value.provinceId)?.regionName || ''
    formData.value.city = cities.value.find((item) => item.id === formData.value.cityId)?.regionName || ''
    formData.value.county = counties.value.find((item) => item.id === formData.value.countyId)?.regionName || ''

    modifyInternetHospitalSetting(unref(formData)).then(() => {
      Message.success('修改互联网医院配置成功')
    })
  })
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
