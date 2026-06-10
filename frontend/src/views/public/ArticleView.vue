<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import { getArticleDetail, getPublicCategories } from '@/api'
import { toast } from '@/utils/toast'
import { formatDate } from '@/utils/format'

const route = useRoute()
const router = useRouter()

const article = ref(null)
const categories = ref([])
const loading = ref(true)

marked.setOptions({ breaks: true, gfm: true })

const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  return marked.parse(article.value.content)
})

const categoryName = computed(() => {
  if (!article.value) return ''
  const c = categories.value.find((x) => x.id === article.value.categoryId)
  return c ? c.name : ''
})

async function load(id) {
  loading.value = true
  try {
    const [detail, cats] = await Promise.all([
      getArticleDetail(id),
      getPublicCategories().catch(() => []),
    ])
    article.value = detail
    categories.value = cats || []
  } catch (e) {
    toast.error(e.message)
    article.value = null
  } finally {
    loading.value = false
  }
}

onMounted(() => load(route.params.id))
watch(
  () => route.params.id,
  (id) => id && load(id)
)
</script>

<template>
  <div class="container article-page">
    <button class="back-link" @click="router.back()">← 返回</button>

    <div v-if="loading" class="state-box">正在加载…</div>
    <div v-else-if="!article" class="state-box">文章不存在或已被删除</div>

    <article v-else>
      <header class="article-header">
        <div class="head-meta">
          <span v-if="categoryName" class="tag-chip">{{ categoryName }}</span>
          <span class="head-date">{{ formatDate(article.createTime) }}</span>
          <span class="head-views">{{ article.viewCount || 0 }} 次阅读</span>
        </div>
        <h1 class="article-h1 serif">{{ article.title }}</h1>
        <p v-if="article.summary" class="article-lead">{{ article.summary }}</p>
      </header>

      <div class="markdown-body" v-html="renderedContent"></div>
    </article>
  </div>
</template>

<style scoped>
.article-page {
  max-width: 800px;
  padding-top: 32px;
  padding-bottom: 48px;
}
.back-link {
  color: var(--muted);
  font-size: 14px;
  margin-bottom: 24px;
  transition: color 0.18s ease;
}
.back-link:hover {
  color: var(--accent);
}
.article-header {
  padding-bottom: 28px;
  border-bottom: 1px solid var(--border);
  margin-bottom: 32px;
}
.head-meta {
  display: flex;
  align-items: center;
  gap: 14px;
  font-size: 13px;
  color: var(--muted);
  margin-bottom: 18px;
}
.article-h1 {
  font-size: 36px;
  font-weight: 700;
  line-height: 1.3;
  letter-spacing: -0.5px;
}
.article-lead {
  margin-top: 16px;
  font-size: 17px;
  color: var(--muted);
  line-height: 1.7;
}
</style>

<!-- 阅读页正文字号略大（markdown-body 基础样式见 global.css） -->
<style scoped>
.article-page :deep(.markdown-body) {
  font-size: 17px;
}
@media (max-width: 640px) {
  .article-h1 {
    font-size: 28px;
  }
  .article-page :deep(.markdown-body) {
    font-size: 16px;
  }
}
</style>
