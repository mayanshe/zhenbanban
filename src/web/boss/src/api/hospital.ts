import axios from 'axios';
import { Pager } from '@/api/common';

// 业务医院表 Hospital

/**
 * 业务医院表交互模型
 */
export interface Hospital {
    id: string;
    ownershipType: string;
    hospitalType: string;
    hospitalLevel: string;
    status: string;
    insuranceCode: string;
    usccCode: string;
    hospitalCode: string;
    hospitalName: string;
    provinceId: number;
    cityId: number;
    countyId: number;
    address: string;
    postalCode: string;
    longitude: number;
    latitude: number;
    mapUrl: string;
    contactPhone: string;
    contactEmail: string;
    website: string;
    companionDiagnosisEnabled: boolean;
    mealServiceEnabled: boolean;
    testingDeliveryEnabled: boolean;
}

/**
 * 业务医院表视图模型
 */
export interface HospitalView extends Hospital {
    createdAt: string;
    updatedAt: string;
}

/**
 * 业务医院表搜索模型
 */
export interface HospitalSearchModel {
    keywords: string;
    hospitalCode: string;
    deleted: boolean;
}

/**
 * 获取业务医院表
 * @param id
 */
export function getHospital(id: string) {
    return axios.get(`/hospitals/${id}`);
}

/**
 * 搜索业务医院表分页
 * @param data
 * @param page
 */
export function getHospitalPagination(data: HospitalSearchModel, page: Pager) {
    return axios.get('/hospitals', { params: { ...data, ...page } });
}

/**
 * 添加业务医院表
 * @param data
 */
export function createHospital(data: Hospital) {
    return axios.post('/hospitals', data);
}

/**
 * 修改业务医院表信息
 * @param data
 */
export function updateHospital(data: Hospital) {
    return axios.put(`/hospitals/${data.id}`, data);
}

/**
 * 删除业务医院表
 * @param id
 */
export function deleteHospital(id: string) {
    return axios.delete(`/hospitals/${id}`);
}
