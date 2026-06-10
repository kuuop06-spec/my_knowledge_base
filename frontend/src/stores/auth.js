import { defineStore } from 'pinia'

// 后端暂无登录接口，这里以前端门禁方式实现后台访问控制。
// 默认账号：admin / admin123 （可按需修改）
const DEMO_USER = 'admin'
const DEMO_PASS = 'admin123'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('kb_token') || '',
    user: JSON.parse(localStorage.getItem('kb_user') || 'null'),
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
  },
  actions: {
    login(username, password) {
      if (username === DEMO_USER && password === DEMO_PASS) {
        const token = 'local-' + btoa(unescape(encodeURIComponent(username))) + '-' + Date.now()
        const user = { nickname: '管理员', username }
        this.token = token
        this.user = user
        localStorage.setItem('kb_token', token)
        localStorage.setItem('kb_user', JSON.stringify(user))
        return true
      }
      return false
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('kb_token')
      localStorage.removeItem('kb_user')
    },
  },
})
