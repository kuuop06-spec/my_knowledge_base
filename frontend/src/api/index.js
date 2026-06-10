import request from '@/utils/request'

/* ========== 前台公共接口（免登录） ========== */

// 获取可见分类（导航栏）
export const getPublicCategories = () => request.get('/api/public/categories')

// 获取所有标签（侧边栏）
export const getPublicTags = () => request.get('/api/public/tags')

// 获取所有已发布文章
export const getPublishedArticles = () => request.get('/api/public/articles')

// 按分类获取文章
export const getArticlesByCategory = (categoryId) =>
  request.get(`/api/public/articles/category/${categoryId}`)

// 获取文章详情（后端会自动 +1 阅读量）
export const getArticleDetail = (id) =>
  request.get(`/api/public/articles/${id}`)

/* ========== 后台分类管理 ========== */
export const adminListCategories = () => request.get('/api/admin/categories')
export const adminGetCategory = (id) => request.get(`/api/admin/categories/${id}`)
export const adminAddCategory = (data) =>
  request.post('/api/admin/categories', data)
export const adminUpdateCategory = (id, data) =>
  request.put(`/api/admin/categories/${id}`, data)
export const adminDeleteCategory = (id) =>
  request.delete(`/api/admin/categories/${id}`)

/* ========== 后台标签管理 ========== */
// 后端无独立 admin 标签列表接口，复用公共标签列表
export const adminListTags = () => request.get('/api/public/tags')
export const adminAddTag = (data) => request.post('/api/admin/tags', data)
export const adminUpdateTag = (id, data) =>
  request.put(`/api/admin/tags/${id}`, data)
export const adminDeleteTag = (id) => request.delete(`/api/admin/tags/${id}`)

/* ========== 后台文章管理 ========== */
// 后端无独立 admin 文章列表接口，复用公共已发布列表
export const adminListArticles = () => request.get('/api/public/articles')
export const adminGetArticle = (id) => request.get(`/api/public/articles/${id}`)
export const adminPublishArticle = (data) =>
  request.post('/api/admin/articles', data)
export const adminUpdateArticle = (id, data) =>
  request.put(`/api/admin/articles/${id}`, data)
export const adminUpdateArticleStatus = (id, status) =>
  request.put(`/api/admin/articles/${id}/status/${status}`)
export const adminDeleteArticle = (id) =>
  request.delete(`/api/admin/articles/${id}`)

/* ========== 后台文件管理 ========== */
export const adminListFiles = () => request.get('/api/admin/files')
export const adminUploadFile = (formData) =>
  request.post('/api/admin/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
export const adminDeleteFile = (id) => request.delete(`/api/admin/files/${id}`)
