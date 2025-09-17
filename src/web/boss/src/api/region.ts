import axios from 'axios'

// 中国行政区划

export interface RegionView {
  id: string
  parentId: string
  regionLevel: string
  postalCode: string
  areaCode: string
  regionName: string
  shortName: string
  mergeName: string
  longitude: string
  latitude: string
}

/**
 * 获取中国行政区划id
 * @param pid
 */
export function getRegionList(pid: string) {
  return axios.get<RegionView[]>('/regions', { params: { parentId: pid } })
}
