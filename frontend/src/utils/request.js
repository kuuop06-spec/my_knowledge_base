import axios from 'axios'

// 后端统一返回 { code, message, data }
// 开发环境通过 Vite 代理转发到 http://localhost:8080
const service = axios.create({
  baseURL: '/',
  timeout: 10000,
})

// 请求拦截器：访问 /api/admin 接口时自动携带 token
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('kb_token')
    if (token && config.url && config.url.includes('/api/admin')) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：统一拆包与错误处理
service.interceptors.response.use(
  (res) => {
    const body = res.data
    // 非标准结构（如文件流）直接返回
    if (body == null || typeof body.code === 'undefined') {
      return body
    }
    if (body.code !== 200) {
      if (body.code === 401) {
        localStorage.removeItem('kb_token')
        localStorage.removeItem('kb_user')
        if (!location.hash.includes('/admin/login')) {
          location.hash = '#/admin/login'
        }
      }
      return Promise.reject(new Error(body.message || '请求失败'))
    }
    return body.data
  },
  (error) => {
    const msg =
      error?.response?.data?.message ||
      error?.message ||
      '网络异常，请检查后端服务是否启动'
    return Promise.reject(new Error(msg))
  }
)

export default service
