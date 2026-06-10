<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import {
  getPublicCategories,
  getPublicTags,
  getPublishedArticles,
  getArticlesByCategory,
} from '@/api'
import { toast } from '@/utils/toast'
import { formatDateShort } from '@/utils/format'

const categories = ref([])
const tags = ref([])
const articles = ref([])
const loading = ref(true)

const activeCategory = ref(null) // null = 全部
const keyword = ref('')

// 分类 id -> name 映射，用于在文章卡片上展示分类名（后端文章未拼接 categoryName）
const categoryMap = computed(() => {
  const m = {}
  categories.value.forEach((c) => (m[c.id] = c.name))
  return m
})

const filteredArticles = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return articles.value
  return articles.value.filter((a) => {
    const t = (a.title || '').toLowerCase()
    const s = (a.summary || '').toLowerCase()
    return t.includes(kw) || s.includes(kw)
  })
})

async function loadArticles(categoryId) {
  loading.value = true
  try {
    articles.value = categoryId
      ? await getArticlesByCategory(categoryId)
      : await getPublishedArticles()
  } catch (e) {
    toast.error(e.message)
    articles.value = []
  } finally {
    loading.value = false
  }
}

function selectCategory(id) {
  activeCategory.value = id
  keyword.value = ''
  loadArticles(id)
}

onMounted(async () => {
  try {
    const [cats, tg] = await Promise.all([
      getPublicCategories().catch(() => []),
      getPublicTags().catch(() => []),
    ])
    categories.value = cats || []
    tags.value = tg || []
  } catch (e) {
    toast.error(e.message)
  }
  await loadArticles(null)
})
</script>

<template>
  <div class="home">
    <!-- Hero -->
    <section class="hero">
      <div class="container">
        <p class="hero-eyebrow">KNOWLEDGE BASE</p>
        <h1 class="hero-title serif">沉淀每一次思考，<br />让知识可被检索。</h1>
        <p class="hero-sub">
          这里收录技术文章、学习笔记与实践总结，按分类与标签组织，随时回顾。
        </p>
      </div>
    </section>

    <!-- 分类导航 -->
    <div class="container">
      <nav class="category-bar">
        <button
          class="cat-item"
          :class="{ active: activeCategory === null }"
          @click="selectCategory(null)"
        >
          全部
        </button>
        <button
          v-for="c in categories"
          :key="c.id"
          class="cat-item"
          :class="{ active: activeCategory === c.id }"
          @click="selectCategory(c.id)"
        >
          {{ c.name }}
        </button>
      </nav>
    </div>

    <!-- 主体：文章列表 + 标签侧栏 -->
    <div class="container main-grid">
      <section class="article-col">
        <div class="list-head">
          <h2 class="list-title serif">
            {{ activeCategory ? categoryMap[activeCategory] : '最新文章' }}
          </h2>
          <div class="search-box">
            <input
              v-model="keyword"
              class="input"
              type="text"
              placeholder="搜索标题或摘要…"
            />
          </div>
        </div>

        <div v-if="loading" class="state-box">正在加载文章…</div>
        <div v-else-if="filteredArticles.length === 0" class="state-box">
          暂无文章
        </div>

        <ul v-else class="article-list">
          <li v-for="a in filteredArticles" :key="a.id">
            <RouterLink :to="`/article/${a.id}`" class="article-card">
              <div class="article-meta">
                <span v-if="categoryMap[a.categoryId]" class="meta-cat">{{
                  categoryMap[a.categoryId]
                }}</span>
                <span class="meta-date">{{ formatDateShort(a.createTime) }}</span>
              </div>
              <h3 class="article-title serif">{{ a.title }}</h3>
              <p class="article-summary">{{ a.summary || '（暂无摘要）' }}</p>
              <div class="article-foot">
                <span class="view-count">{{ a.viewCount || 0 }} 次阅读</span>
                <span class="read-more">阅读全文 →</span>
              </div>
            </RouterLink>
          </li>
        </ul>
      </section>

      <!-- 侧边栏 -->
      <aside class="side-col">
        <div class="side-card card">
          <h4 class="side-title">标签云</h4>
          <div v-if="tags.length" class="tag-cloud">
            <span
              v-for="t in tags"
              :key="t.id"
              class="tag-chip"
              :style="t.color ? { color: t.color, background: t.color + '1a' } : {}"
              >{{ t.name }}</span
            >
          </div>
          <p v-else class="side-empty">暂无标签</p>
        </div>

        <div class="side-card card">
          <h4 class="side-title">关于</h4>
          <p class="side-about">
            一个简洁的个人知识库，用于整理与分享技术内容。后台由
            <span class="serif">Spring Boot</span> 驱动。
          </p>
        </div>
      </aside>
    </div>
  </div>
</template>

<style scoped>
.hero {
  padding: 56px 0 32px;
}
.hero-eyebrow {
  font-size: 13px;
  letter-spacing: 3px;
  color: var(--accent);
  margin-bottom: 14px;
}
.hero-title {
  font-size: 42px;
  font-weight: 600;
  line-height: 1.25;
  letter-spacing: -0.5px;
}
.hero-sub {
  margin-top: 18px;
  max-width: 560px;
  color: var(--muted);
  font-size: 16px;
}

.category-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 18px 0;
  border-top: 1px solid var(--border);
  border-bottom: 1px solid var(--border);
}
.cat-item {
  padding: 7px 16px;
  border-radius: 999px;
  font-size: 14px;
  color: var(--muted);
  border: 1px solid transparent;
  transition: all 0.18s ease;
}
.cat-item:hover {
  color: var(--foreground);
  background-color: var(--surface-2);
}
.cat-item.active {
  background-color: var(--accent);
  color: #fff;
}

.main-grid {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 36px;
  margin-top: 32px;
  align-items: start;
}

.list-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}
.list-title {
  font-size: 24px;
  font-weight: 600;
}
.search-box {
  width: 220px;
}

.article-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.article-card {
  display: block;
  padding: 22px 24px;
  background-color: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow-sm);
  transition: all 0.2s ease;
}
.article-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
  border-color: var(--accent);
}
.article-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  margin-bottom: 10px;
}
.meta-cat {
  color: var(--accent);
  font-weight: 500;
}
.meta-date {
  color: var(--muted);
}
.article-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}
.article-summary {
  color: var(--muted);
  font-size: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.article-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16px;
  font-size: 13px;
  color: var(--muted);
}
.read-more {
  color: var(--accent);
  font-weight: 500;
}

.side-col {
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: sticky;
  top: 84px;
}
.side-card {
  padding: 20px;
}
.side-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 14px;
}
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.side-about {
  font-size: 14px;
  color: var(--muted);
  line-height: 1.7;
}
.side-empty {
  font-size: 14px;
  color: var(--muted);
}

@media (max-width: 880px) {
  .main-grid {
    grid-template-columns: 1fr;
  }
  .side-col {
    position: static;
  }
  .hero-title {
    font-size: 32px;
  }
  .list-head {
    flex-direction: column;
    align-items: flex-start;
  }
  .search-box {
    width: 100%;
  }
}
</style>
