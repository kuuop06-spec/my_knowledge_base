<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { toast } from '@/utils/toast'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const username = ref('admin')
const password = ref('')
const loading = ref(false)

function handleLogin() {
  if (!username.value || !password.value) {
    toast.error('请输入账号和密码')
    return
  }
  loading.value = true
  setTimeout(() => {
    const ok = auth.login(username.value, password.value)
    loading.value = false
    if (ok) {
      toast.success('登录成功')
      router.replace(route.query.redirect || '/admin')
    } else {
      toast.error('账号或密码错误')
    }
  }, 300)
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-brand">
        <span class="brand-mark">KB</span>
        <h1 class="serif">知识库后台</h1>
        <p>登录以管理你的文章、分类与文件</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div>
          <label class="label">账号</label>
          <input v-model="username" class="input" placeholder="admin" />
        </div>
        <div>
          <label class="label">密码</label>
          <input
            v-model="password"
            type="password"
            class="input"
            placeholder="请输入密码"
          />
        </div>
        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          {{ loading ? '登录中…' : '登 录' }}
        </button>
      </form>

      <p class="login-hint">演示账号：admin / admin123</p>
      <RouterLink to="/" class="back-home">← 返回前台首页</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-color: var(--base);
}
.login-card {
  width: 100%;
  max-width: 400px;
  background-color: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 40px 36px;
  box-shadow: var(--shadow-md);
}
.login-brand {
  text-align: center;
  margin-bottom: 28px;
}
.brand-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background-color: var(--accent);
  color: #fff;
  font-weight: 700;
  font-size: 18px;
  margin-bottom: 16px;
}
.login-brand h1 {
  font-size: 24px;
  font-weight: 600;
}
.login-brand p {
  margin-top: 8px;
  font-size: 14px;
  color: var(--muted);
}
.login-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.login-btn {
  margin-top: 6px;
  width: 100%;
  padding: 11px;
  font-size: 15px;
}
.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.login-hint {
  margin-top: 20px;
  text-align: center;
  font-size: 13px;
  color: var(--muted);
}
.back-home {
  display: block;
  text-align: center;
  margin-top: 14px;
  font-size: 13px;
  color: var(--accent);
}
</style>
