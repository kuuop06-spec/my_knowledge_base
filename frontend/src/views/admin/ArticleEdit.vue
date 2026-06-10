<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import {
  adminGetArticle,
  adminPublishArticle,
  adminUpdateArticle,
  adminListCategories,
  adminListTags,
} from '@/api'
import { toast } from '@/utils/toast'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const categories = ref([])
const tags = ref([])
const saving = ref(false)
const showPreview = ref(false)

const form = ref({
  title: '',
  summary: '',
  content: '',
  categoryId: null,
  tagIds: [],
})

marked.setOptions({ breaks: true, gfm: true })
const preview = computed(() =>
  form.value.content ? marked.parse(form.value.content) : '<p style="color:var(--muted)">（预览区）开始输入正文…</p>'
)

function toggleTag(id) {
  const idx = form.value.tagIds.indexOf(id)
  if (idx > -1) form.value.tagIds.splice(idx, 1)
  else form.value.tagIds.push(id)
}

async function load() {
  try {
    const [cats, tg] = await Promise.all([
      adminListCategories().catch(() => []),
      adminListTags().catch(() => []),
    ])
    categories.value = cats || []
    tags.value = tg || []

    if (isEdit.value) {
      const a = await adminGetArticle(route.params.id)
      form.value.title = a.title || ''
      form.value.summary = a.summary || ''
      form.value.content = a.content || ''
      form.value.categoryId = a.categoryId ?? null
      // 后端文章详情未返回标签关联，编辑时标签默认留空，可重新选择
      form.value.tagIds = Array.isArray(a.tagIds) ? a.tagIds : []
    }
  } catch (e) {
    toast.error(e.message)
  }
}

async function handleSave() {
  if (!form.value.title.trim()) {
    toast.error('请填写标题')
    return
  }
  if (!form.value.content.trim()) {
    toast.error('请填写正文内容')
    return
  }
  saving.value = true
  const payload = {
    title: form.value.title,
    summary: form.value.summary,
    content: form.value.content,
    categoryId: form.value.categoryId,
    tagIds: form.value.tagIds,
  }
  try {
    if (isEdit.value) {
      await adminUpdateArticle(route.params.id, payload)
      toast.success('文章已更新')
    } else {
      await adminPublishArticle(payload)
      toast.success('文章已发布')
    }
    router.push({ name: 'admin-articles' })
  } catch (e) {
    toast.error(e.message)
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="page edit-page">
    <div class="page-head">
      <div>
        <h1 class="page-title serif">{{ isEdit ? '编辑文章' : '新建文章' }}</h1>
        <p class="page-sub">支持 Markdown 语法，可实时预览</p>
      </div>
      <div class="head-actions">
        <button class="btn btn-ghost" @click="router.back()">取消</button>
        <button class="btn btn-primary" :disabled="saving" @click="handleSave">
          {{ saving ? '保存中…' : isEdit ? '保存修改' : '发布文章' }}
        </button>
      </div>
    </div>

    <div class="card form-card">
      <div class="form-row">
        <label class="label">标题</label>
        <input v-model="form.title" class="input" placeholder="请输入文章标题" />
      </div>

      <div class="form-row">
        <label class="label">摘要</label>
        <textarea
          v-model="form.summary"
          class="input"
          rows="2"
          placeholder="一句话概括文章内容（可选）"
        ></textarea>
      </div>

      <div class="form-grid">
        <div class="form-row">
          <label class="label">分类</label>
          <select v-model="form.categoryId" class="input">
            <option :value="null">未分类</option>
            <option v-for="c in categories" :key="c.id" :value="c.id">
              {{ c.name }}
            </option>
          </select>
        </div>
        <div class="form-row">
          <label class="label">标签</label>
          <div class="tag-picker">
            <span
              v-for="t in tags"
              :key="t.id"
              class="pick-chip"
              :class="{ active: form.tagIds.includes(t.id) }"
              @click="toggleTag(t.id)"
              >{{ t.name }}</span
            >
            <span v-if="!tags.length" class="side-empty">暂无标签，请先到标签管理添加</span>
          </div>
        </div>
      </div>

      <div class="form-row">
        <div class="editor-head">
          <label class="label" style="margin: 0">正文（Markdown）</label>
          <button class="btn btn-ghost btn-sm" @click="showPreview = !showPreview">
            {{ showPreview ? '隐藏预览' : '显示预览' }}
          </button>
        </div>
        <div class="editor-wrap" :class="{ split: showPreview }">
          <textarea
            v-model="form.content"
            class="input editor-textarea"
            placeholder="# 开始写作&#10;支持标准 Markdown 语法…"
          ></textarea>
          <div v-if="showPreview" class="editor-preview markdown-body" v-html="preview"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import '@/styles/admin-page.css';
.edit-page {
  max-width: 1100px;
}
.head-actions {
  display: flex;
  gap: 10px;
}
.form-card {
  padding: 24px;
}
.tag-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 0;
}
.pick-chip {
  padding: 5px 12px;
  border-radius: 999px;
  font-size: 13px;
  border: 1px solid var(--border);
  color: var(--muted);
  cursor: pointer;
  transition: all 0.16s ease;
}
.pick-chip:hover {
  border-color: var(--accent);
}
.pick-chip.active {
  background-color: var(--accent);
  color: #fff;
  border-color: var(--accent);
}
.side-empty {
  font-size: 13px;
  color: var(--muted);
}
.editor-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}
.editor-wrap.split {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.editor-textarea {
  min-height: 440px;
  font-family: "SFMono-Regular", Consolas, monospace;
  font-size: 14px;
  line-height: 1.7;
  resize: vertical;
}
.editor-preview {
  min-height: 440px;
  max-height: 600px;
  overflow-y: auto;
  padding: 14px 18px;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  background-color: var(--surface-2);
}
@media (max-width: 720px) {
  .editor-wrap.split {
    grid-template-columns: 1fr;
  }
}
</style>
