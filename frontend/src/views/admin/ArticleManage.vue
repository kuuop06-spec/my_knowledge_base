<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  adminListArticles,
  adminDeleteArticle,
  adminUpdateArticleStatus,
  adminListCategories,
} from '@/api'
import { toast } from '@/utils/toast'
import { formatDateShort } from '@/utils/format'
import BaseModal from '@/components/BaseModal.vue'

const router = useRouter()
const articles = ref([])
const categories = ref([])
const loading = ref(true)
const keyword = ref('')

const deleteTarget = ref(null)

const categoryMap = computed(() => {
  const m = {}
  categories.value.forEach((c) => (m[c.id] = c.name))
  return m
})

const filtered = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return articles.value
  return articles.value.filter((a) =>
    (a.title || '').toLowerCase().includes(kw)
  )
})

async function load() {
  loading.value = true
  try {
    const [list, cats] = await Promise.all([
      adminListArticles(),
      adminListCategories().catch(() => []),
    ])
    articles.value = list || []
    categories.value = cats || []
  } catch (e) {
    toast.error(e.message)
  } finally {
    loading.value = false
  }
}

function goNew() {
  router.push({ name: 'admin-article-new' })
}
function goEdit(id) {
  router.push({ name: 'admin-article-edit', params: { id } })
}

async function toggleStatus(a) {
  const next = a.status === 1 ? 0 : 1
  try {
    await adminUpdateArticleStatus(a.id, next)
    a.status = next
    toast.success(next === 1 ? '已上线' : '已下线')
  } catch (e) {
    toast.error(e.message)
  }
}

async function confirmDelete() {
  const id = deleteTarget.value.id
  try {
    await adminDeleteArticle(id)
    articles.value = articles.value.filter((a) => a.id !== id)
    toast.success('删除成功')
  } catch (e) {
    toast.error(e.message)
  } finally {
    deleteTarget.value = null
  }
}

onMounted(load)
</script>

<template>
  <div class="page">
    <div class="page-head">
      <div>
        <h1 class="page-title serif">文章管理</h1>
        <p class="page-sub">管理已发布的文章内容</p>
      </div>
      <button class="btn btn-primary" @click="goNew">+ 新建文章</button>
    </div>

    <div class="toolbar">
      <input
        v-model="keyword"
        class="input"
        style="max-width: 280px"
        placeholder="按标题搜索…"
      />
    </div>

    <div class="card table-card">
      <div v-if="loading" class="state-box">正在加载…</div>
      <div v-else-if="filtered.length === 0" class="state-box">暂无文章</div>
      <table v-else class="data-table">
        <thead>
          <tr>
            <th style="width: 40%">标题</th>
            <th>分类</th>
            <th>阅读量</th>
            <th>状态</th>
            <th>创建时间</th>
            <th style="text-align: right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="a in filtered" :key="a.id">
            <td>
              <div class="cell-title">{{ a.title }}</div>
              <div class="cell-summary">{{ a.summary }}</div>
            </td>
            <td>{{ categoryMap[a.categoryId] || '—' }}</td>
            <td>{{ a.viewCount || 0 }}</td>
            <td>
              <span
                class="status-dot"
                :class="a.status === 1 ? 'on' : 'off'"
              ></span>
              {{ a.status === 1 ? '已发布' : '草稿' }}
            </td>
            <td>{{ formatDateShort(a.createTime) }}</td>
            <td class="cell-actions">
              <button class="btn btn-ghost btn-sm" @click="toggleStatus(a)">
                {{ a.status === 1 ? '下线' : '上线' }}
              </button>
              <button class="btn btn-ghost btn-sm" @click="goEdit(a.id)">
                编辑
              </button>
              <button class="btn btn-danger btn-sm" @click="deleteTarget = a">
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BaseModal
      :show="!!deleteTarget"
      title="确认删除"
      width="400px"
      @close="deleteTarget = null"
    >
      <p>确定要删除文章「{{ deleteTarget?.title }}」吗？此操作不可恢复。</p>
      <template #footer>
        <button class="btn btn-ghost" @click="deleteTarget = null">取消</button>
        <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
      </template>
    </BaseModal>
  </div>
</template>

<style scoped>
@import '@/styles/admin-page.css';
.cell-title {
  font-weight: 500;
}
.cell-summary {
  font-size: 12.5px;
  color: var(--muted);
  margin-top: 3px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  max-width: 380px;
}
.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 5px;
}
.status-dot.on {
  background: var(--accent);
}
.status-dot.off {
  background: #c9c2b4;
}
</style>
