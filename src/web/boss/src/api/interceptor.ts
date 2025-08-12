import { getToken } from '@/utils/auth'
import { Message } from '@arco-design/web-vue'
import axios from 'axios'

if (import.meta.env.VITE_API_BASE_URL) {
  axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL
}

// 请求拦截器
axios.interceptors.request.use(
  (config: any) => {
    const token = getToken()

    if (token) {
      if (!config.headers) {
        config.headers = {}
      }

      config.headers.Authorization = `${token}`
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axios.interceptors.response.use(
  (response: any) => {
    return response.data
  },
  (error) => {
    if (error.response) {
      Message.error({
        content: error.response.data?.message || '尚未标记的错误，请重试或联系管理员',
        duration: 5 * 1000,
      })
    } else {
      Message.error({
        content: '尚未标记的错误，请重试或联系管理员',
        duration: 5 * 1000,
      })
    }

    return Promise.reject(error)
  }
)
