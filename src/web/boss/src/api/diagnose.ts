import axios from 'axios';
import { Pager } from '@/api/common';

// 疾病诊断Diagnose

/**
 * 疾病诊断交互模型
 */
export interface Diagnose {
    id: string; // 疾病诊断ID
    icdType: number; // ICD类型
    icdCode: string; // ICD编码
    icdName: string; // ICD名称
    icdNamePinyin: string; // ICD名称拼音
    icdNamePinyinAbbr: string; // ICD名称拼音首字母缩写
    icdOptionalName: string; // ICD疾病可选用名
    icdOptionalNamePinyin: string; // ICD疾病可选用名拼音
    icdOptionalNamePinyinAbbr: string; // ICD疾病可选用名拼音首字母
    icdAliasName: string; // ICD疾病别名
    icdAliasNamePinyin: string; // ICD疾病别名拼音
    icdAliasNamePinyinAbbr: string; // ICD疾病别名拼音首字母
    description: string; // 疾病描述
    chapterCode: string; // ICD-10章节编码
    chapterName: string; // ICD-10章节名称
    blockCode: string; // ICD-10疾病组编码
    blockName: string; // ICD-10疾病组名称
}

/**
 * 疾病诊断视图模型
 */
export interface DiagnoseView extends Diagnose {
    createdAt: string; // 创建时间
    updatedAt: string; // 修改时间
}

/**
 * 疾病诊断搜索模型
 */
export interface DiagnoseSearchModel {
    keywords: string; // 关键词
    icdType: number | null; // ICD类型
}

/**
 * 获取疾病诊断
 * @param id
 */
export function getDiagnose(id: string) {
    return axios.get(`/diagnoses/${id}`);
}

/**
 * 搜索疾病诊断分页
 * @param data
 * @param page
 */
export function getDiagnosePagination(data: DiagnoseSearchModel, page: Pager) {
    return axios.get('/diagnoses', { params: { ...data, ...page } });
}

/**
 * 添加疾病诊断
 * @param data
 */
export function createDiagnose(data: Diagnose) {
    return axios.post('/diagnoses', data);
}

/**
 * 修改疾病诊断信息
 * @param data
 */
export function updateDiagnose(data: Diagnose) {
    return axios.put(`/diagnoses/${data.id}`, data);
}

/**
 * 删除疾病诊断
 * @param id
 */
export function deleteDiagnose(id: string) {
    return axios.delete(`/diagnoses/${id}`);
}
