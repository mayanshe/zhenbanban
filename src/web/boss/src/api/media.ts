import axios from 'axios'
import { Pager, Pagination, ValueObject } from '@/api/common'

// 账号（Account）

/**
 * 媒体
 */
export interface Media {
    id: string
    fileName: string
    description: string
}

/**
 * 媒体视图
 */
export interface MediaView {
    id: string
    mediaType: ValueObject
    fileMd5: string
    fileName: string
    filePath: string
    fileSize: string
    fileExtension: string
    mimeType: string
    url: string
    thumbnailUrl: string
    description: string
    createdAt: string
    updatedAt: string
}

/**
 * 上传文件
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const upload = (file: File, isPublicRead: boolean, url: string = '/medias') => {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest()
        const formData = new FormData()
        formData.append('file', file)

        xhr.upload.onprogress = (event) => {
            if (event.total > 0) {
                const percent = event.loaded / event.total
            }
        }

        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response)
            } else {
                reject(new Error(xhr.responseText))
            }
        };

        xhr.onerror = (e) => reject(e)

        xhr.open('POST', url, true)
        xhr.send(formData)
    })
}

/**
 * 上传图片
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const uploadImage = (file: File, isPublicRead: boolean, url: string = '/medias/images') => {
    return upload(file, isPublicRead, url)
}

/**
 * 上传视频
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const uploadVideo = (file: File, isPublicRead: boolean, url: string = '/medias/videos') => {
    return upload(file, isPublicRead, url)
}

/**
 * 上传音频
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const uploadAudio = (file: File, isPublicRead: boolean, url: string = '/medias/audios') => {
    return upload(file, isPublicRead, url)
}

/**
 * 上传文档
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const uploadDocument = (file: File, isPublicRead: boolean, url: string = '/medias/audios') => {
    return upload(file, isPublicRead, url)
}

/**
 * 上传压缩包
 *
 * @param file
 * @param isPublicRead
 * @param url
 */
export const uploadArchive = (file: File, isPublicRead: boolean, url: string = '/medias/audios') => {
    return upload(file, isPublicRead, url)
}

/**
 * 修改媒体
 *
 * @param data
 */
export function modifyMedia(data: Media) {
    return axios.patch(`/medias/${data.id}`, data)
}

/**
 * 删除媒体
 *
 * @param id
 */
export function deleteMedia(id: string) {
    return axios.delete(`/medias/${id}`)
}