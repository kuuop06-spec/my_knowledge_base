<script setup>
import { RouterView, RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { toast } from '@/utils/toast'
import ToastHost from '@/components/ToastHost.vue'
import IconBase from '@/components/IconBase.vue'

const auth = useAuthStore()
const router = useRouter()

const navItems = [
  { name: 'admin-articles', label: '文章管理', icon: 'article' },
  { name: 'admin-categories', label: '分类管理', icon: 'category' },
  { name: 'admin-tags', label: '标签管理', icon: 'tag' },
  { name: 'admin-files', label: '文件管理', icon: 'file' },
]

function handleLogout() {
  auth.logout()
  toast.info('已退出登录')
  router.replace('/admin/login')
}
</script>

<template>
  <div class="admin-layout">
    <aside class="sidebar">
      <div class="sidebar-brand">
        <span class="brand-mark">KB</span>
        <span class="serif">管理后台</span>
      </div>
      <nav class="sidebar-nav">
        <RouterLink
          v-for="item in navItems"
          :key="item.name"
          :to="{ name: item.name }"
          class="side-item"
          active-class="active"
        >
          <span class="side-icon"><IconBase :name="item.icon" :size="18" /></span>
          {{ item.label }}
        </RouterLink>
      </nav>
      <div class="sidebar-foot">
        <RouterLink to="/" class="side-item side-sub">→ 查看前台</RouterLink>
      </div>
    </aside>

    <div class="admin-main">
      <header class="admin-topbar">
        <div class="topbar-title">控制台</div>
        <div class="topbar-user">
          <span class="user-name">{{ auth.user?.nickname || '管理员' }}</span>
          <button class="btn btn-ghost btn-sm" @click="handleLogout">退出</button>
        </div>
      </header>
      <main class="admin-content">
        <RouterView v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </RouterView>
      </main>
    </div>

    <ToastHost />
  </div>
</template>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}
.sidebar {
  width: 230px;
  flex-shrink: 0;
  background-color: var(--surface);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  height: 100vh;
}
.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 22px 22px;
  border-bottom: 1px solid var(--border);
  font-size: 18px;
  font-weight: 600;
}
.brand-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background-color: var(--accent);
  color: #fff;
  font-weight: 700;
  font-size: 13px;
}
.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.side-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: var(--radius);
  font-size: 14.5px;
  color: var(--muted);
  transition: all 0.18s ease;
}
.side-item:hover {
  background-color: var(--surface-2);
  color: var(--foreground);
}
.side-item.active {
  background-color: var(--accent-soft);
  color: var(--accent);
  font-weight: 500;
}
.side-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
}
.sidebar-foot {
  padding: 12px;
  border-top: 1px solid var(--border);
}
.side-sub {
  font-size: 13px;
}
.admin-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.admin-topbar {
  height: 60px;
  background-color: var(--surface);
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 20;
}
.topbar-title {
  font-size: 15px;
  font-weight: 500;
  color: var(--muted);
}
.topbar-user {
  display: flex;
  align-items: center;
  gap: 14px;
}
.user-name {
  font-size: 14px;
}
.admin-content {
  flex: 1;
  padding: 28px;
}
@media (max-width: 720px) {
  .sidebar {
    width: 66px;
  }
  .sidebar-brand .serif,
  .side-item:not(.active) {
    /* keep labels but compact */
  }
  .sidebar-brand {
    padding: 18px 0;
    justify-content: center;
  }
  .sidebar-brand .serif {
    display: none;
  }
  .side-item {
    flex-direction: column;
    gap: 4px;
    font-size: 11px;
    padding: 10px 4px;
    text-align: center;
  }
  .admin-content {
    padding: 16px;
  }
}
</style>
