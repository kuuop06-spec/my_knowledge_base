<script setup>
import { RouterView, RouterLink } from 'vue-router'
import ToastHost from '@/components/ToastHost.vue'
</script>

<template>
  <div class="public-layout">
    <header class="site-header">
      <div class="container header-inner">
        <RouterLink to="/" class="brand">
          <span class="brand-mark">KB</span>
          <span class="brand-text serif">知识库</span>
        </RouterLink>
        <nav class="header-nav">
          <a
            href="https://github.com"
            target="_blank"
            rel="noopener"
            class="nav-link"
            >GitHub</a
          >
          <RouterLink to="/admin" class="nav-link nav-admin">后台管理</RouterLink>
        </nav>
      </div>
    </header>

    <main>
      <RouterView />
    </main>

    <footer class="site-footer">
      <div class="container footer-inner">
        <span class="serif">知识库 · Knowledge Base</span>
        <span class="footer-muted">沉淀技术，记录成长</span>
      </div>
    </footer>

    <ToastHost />
  </div>
</template>

<style scoped>
.public-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.site-header {
  position: sticky;
  top: 0;
  z-index: 50;
  background-color: rgba(244, 241, 234, 0.85);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border);
}
.header-inner {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}
.brand-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 8px;
  background-color: var(--accent);
  color: #fff;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 0.5px;
}
.brand-text {
  font-size: 20px;
  font-weight: 600;
}
.header-nav {
  display: flex;
  align-items: center;
  gap: 8px;
}
.nav-link {
  padding: 7px 14px;
  border-radius: var(--radius);
  font-size: 14px;
  color: var(--muted);
  transition: all 0.18s ease;
}
.nav-link:hover {
  color: var(--foreground);
  background-color: var(--surface-2);
}
.nav-admin {
  color: var(--accent);
  border: 1px solid var(--border);
}
.nav-admin:hover {
  background-color: var(--accent-soft);
  color: var(--accent);
}
main {
  flex: 1;
}
.site-footer {
  border-top: 1px solid var(--border);
  background-color: var(--surface-2);
  padding: 28px 0;
  margin-top: 48px;
}
.footer-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  color: var(--muted);
}
.footer-inner .serif {
  color: var(--foreground);
  font-size: 16px;
}
.footer-muted {
  font-size: 13px;
}
@media (max-width: 640px) {
  .footer-inner {
    flex-direction: column;
    gap: 6px;
  }
}
</style>
