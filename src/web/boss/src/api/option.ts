import axios from 'axios'

// 配置Option

/**
 * 互联网医院配置
 */
export interface InternetHospitalSetting {
  hospitalName: string
  licenseNumber: string
  provinceId: string
  province: string
  cityId: string
  city: string
  countyId: string
  county: string
  address: string
  contactNumbers: string
  serviceTimes: string
  website: string
  email: string
  introduction: string
}

/**
 * 互联网医院配置View
 */
export interface InternetHospitalSettingView {
  hospitalName: string
  licenseNumber: string
  provinceId: string
  province: string
  cityId: string
  city: string
  countyId: string
  county: string
  address: string
  contactNumbers: string[]
  serviceTimes: string[]
  website: string
  email: string
  introduction: string
}

/**
 * 获取互联网医院配置
 */
export function getInternetHospitalSetting() {
  return axios.get<InternetHospitalSettingView>('/options/internet-hospital-setting')
}

/**
 * 修改互联网医院配置
 * @param data
 */
export function modifyInternetHospitalSetting(data: InternetHospitalSetting) {
  return axios.put('/options/internet-hospital-setting', data)
}
