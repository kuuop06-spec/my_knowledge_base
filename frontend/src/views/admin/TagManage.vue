<script setup>
import { ref, onMounted } from 'vue'
import {
  adminListTags,
  adminAddTag,
  adminUpdateTag,
  adminDeleteTag,
} from '@/api'
import { toast } from '@/utils/toast'
import BaseModal from '@/components/BaseModal.vue'

const list = ref([])
const loading = ref(true)
const showForm = ref(false)
const editing = ref(null)
const deleteTarget = ref(null)

const PRESET_COLORS = ['#2f6f5e', '#b4452f', '#3a6ea5', '#9a6b2f', '#6b5b95', '#3f8f5f']
const form = ref({ name: '', color: '#2f6f5e' })

async function load() {
  loading.value = true
  try {
    list.value = (await adminListTags()) || []
  } catch (e) {
    toast.error(e.message)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editing.value = null
  form.value = { name: '', color: '#2f6f5e' }
  showForm.value = true
}
function openEdit(t) {
  editing.value = t
  form.value = { name: t.name, color: t.color || '#2f6f5e' }
  showForm.value = true
}

async function save() {
  if (!form.value.name.trim()) {
    toast.error('请填写标签名称')
    return
  }
  const payload = { name: form.value.name, color: form.value.color }
  try {
    if (editing.value) {
      await adminUpdateTag(editing.value.id, payload)
      toast.success('标签已更新')
    } else {
      await adminAddTag(payload)
      toast.success('标签已添加')
    }
    showForm.value = false
    await load()
  } catch (e) {
    toast.error(e.message)
  }
}

async function confirmDelete() {
  try {
    await adminDeleteTag(deleteTarget.value.id)
    toast.success('删除成功')
    list.value = list.value.filter((t) => t.id !== deleteTarget.value.id)
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
        <h1 class="page-title serif">标签管理</h1>
        <p class="page-sub">为文章打上灵活的标签</p>
      </div>
      <button class="btn btn-primary" @click="openCreate">+ 新建标签</button>
    </div>

    <div v-if="loading" class="state-box">正在加载…</div>
    <div v-else-if="list.length === 0" class="card state-box">暂无标签</div>
    <div v-else class="tag-grid">
      <div v-for="t in list" :key="t.id" class="tag-item card">
        <span
          class="tag-dot"
          :style="{ background: t.color || 'var(--accent)' }"
        ></span>
        <span class="tag-name">{{ t.name }}</span>
        <div class="tag-ops">
          <button class="btn btn-ghost btn-sm" @click="openEdit(t)">编辑</button>
          <button class="btn btn-danger btn-sm" @click="deleteTarget = t">删除</button>
        </div>
      </div>
    </div>

    <BaseModal
      :show="showForm"
      :title="editing ? '编辑标签' : '新建标签'"
      width="420px"
      @close="showForm = false"
    >
      <div class="form-row">
        <label class="label">标签名称</label>
        <input v-model="form.name" class="input" placeholder="如：Vue3" />
      </div>
      <div class="form-row">
        <label class="label">颜色</label>
        <div class="color-row">
          <span
            v-for="c in PRESET_COLORS"
            :key="c"
            class="color-dot"
            :class="{ active: form.color === c }"
            :style="{ background: c }"
            @click="form.color = c"
          ></span>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showForm = false">取消</button>
        <button class="btn btn-primary" @click="save">保存</button>
      </template>
    </BaseModal>

    <BaseModal :show="!!deleteTarget" title="确认删除" width="400px" @close="deleteTarget = null">
      <p>确定删除标签「{{ deleteTarget?.name }}」吗？</p>
      <template #footer>
        <button class="btn btn-ghost" @click="deleteTarget = null">取消</button>
        <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
      </template>
    </BaseModal>
  </div>
</template>

<style scoped>
@import '@/styles/admin-page.css';
.tag-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 12px;
}
.tag-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 16px;
}
.tag-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}
.tag-name {
  font-weight: 500;
  flex: 1;
}
.tag-ops {
  display: flex;
  gap: 6px;
}
.color-row {
  display: flex;
  gap: 10px;
}
.color-dot {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  border: 2px solid transparent;
  transition: transform 0.15s ease;
}
.color-dot:hover {
  transform: scale(1.1);
}
.color-dot.active {
  border-color: var(--foreground);
}
</style>
