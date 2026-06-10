import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/PublicLayout.vue'),
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('@/views/public/HomeView.vue'),
      },
      {
        path: 'article/:id',
        name: 'article',
        component: () => import('@/views/public/ArticleView.vue'),
      },
    ],
  },
  {
    path: '/admin/login',
    name: 'admin-login',
    component: () => import('@/views/admin/LoginView.vue'),
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/admin/articles',
      },
      {
        path: 'articles',
        name: 'admin-articles',
        component: () => import('@/views/admin/ArticleManage.vue'),
      },
      {
        path: 'articles/new',
        name: 'admin-article-new',
        component: () => import('@/views/admin/ArticleEdit.vue'),
      },
      {
        path: 'articles/edit/:id',
        name: 'admin-article-edit',
        component: () => import('@/views/admin/ArticleEdit.vue'),
      },
      {
        path: 'categories',
        name: 'admin-categories',
        component: () => import('@/views/admin/CategoryManage.vue'),
      },
      {
        path: 'tags',
        name: 'admin-tags',
        component: () => import('@/views/admin/TagManage.vue'),
      },
      {
        path: 'files',
        name: 'admin-files',
        component: () => import('@/views/admin/FileManage.vue'),
      },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

// 路由守卫：后台需要登录
router.beforeEach((to) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('kb_token')
    if (!token) {
      return { name: 'admin-login', query: { redirect: to.fullPath } }
    }
  }
  return true
})

export default router
