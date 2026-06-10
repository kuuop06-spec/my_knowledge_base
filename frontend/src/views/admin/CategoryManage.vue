<script setup>
import { ref, onMounted } from 'vue'
import {
  adminListCategories,
  adminAddCategory,
  adminUpdateCategory,
  adminDeleteCategory,
} from '@/api'
import { toast } from '@/utils/toast'
import BaseModal from '@/components/BaseModal.vue'

const list = ref([])
const loading = ref(true)
const showForm = ref(false)
const editing = ref(null)
const deleteTarget = ref(null)

const form = ref({ name: '', description: '', sortOrder: 0, isVisible: 1 })

async function load() {
  loading.value = true
  try {
    list.value = (await adminListCategories()) || []
  } catch (e) {
    toast.error(e.message)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editing.value = null
  form.value = { name: '', description: '', sortOrder: 0, isVisible: 1 }
  showForm.value = true
}
function openEdit(c) {
  editing.value = c
  form.value = {
    name: c.name,
    description: c.description || '',
    sortOrder: c.sortOrder ?? 0,
    isVisible: c.isVisible ?? 1,
  }
  showForm.value = true
}

async function save() {
  if (!form.value.name.trim()) {
    toast.error('请填写分类名称')
    return
  }
  const payload = {
    name: form.value.name,
    description: form.value.description,
    sortOrder: Number(form.value.sortOrder) || 0,
    isVisible: Number(form.value.isVisible),
  }
  try {
    if (editing.value) {
      await adminUpdateCategory(editing.value.id, payload)
      toast.success('分类已更新')
    } else {
      await adminAddCategory(payload)
      toast.success('分类已添加')
    }
    showForm.value = false
    await load()
  } catch (e) {
    toast.error(e.message)
  }
}

async function confirmDelete() {
  try {
    await adminDeleteCategory(deleteTarget.value.id)
    toast.success('删除成功')
    list.value = list.value.filter((c) => c.id !== deleteTarget.value.id)
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
        <h1 class="page-title serif">分类管理</h1>
        <p class="page-sub">用于组织文章的顶级分类</p>
      </div>
      <button class="btn btn-primary" @click="openCreate">+ 新建分类</button>
    </div>

    <div class="card table-card">
      <div v-if="loading" class="state-box">正在加载…</div>
      <div v-else-if="list.length === 0" class="state-box">暂无分类</div>
      <table v-else class="data-table">
        <thead>
          <tr>
            <th>名称</th>
            <th>描述</th>
            <th>排序</th>
            <th>可见</th>
            <th style="text-align: right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td><strong>{{ c.name }}</strong></td>
            <td class="muted-cell">{{ c.description || '—' }}</td>
            <td>{{ c.sortOrder ?? 0 }}</td>
            <td>
              <span class="tag-chip" v-if="c.isVisible">显示</span>
              <span class="tag-chip hidden-chip" v-else>隐藏</span>
            </td>
            <td class="cell-actions">
              <button class="btn btn-ghost btn-sm" @click="openEdit(c)">编辑</button>
              <button class="btn btn-danger btn-sm" @click="deleteTarget = c">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BaseModal
      :show="showForm"
      :title="editing ? '编辑分类' : '新建分类'"
      @close="showForm = false"
    >
      <div class="form-row">
        <label class="label">分类名称</label>
        <input v-model="form.name" class="input" placeholder="如：后端技术" />
      </div>
      <div class="form-row">
        <label class="label">描述</label>
        <textarea v-model="form.description" class="input" rows="2" placeholder="分类简介（可选）"></textarea>
      </div>
      <div class="form-grid">
        <div class="form-row">
          <label class="label">排序值（越小越靠前）</label>
          <input v-model="form.sortOrder" type="number" class="input" />
        </div>
        <div class="form-row">
          <label class="label">是否可见</label>
          <select v-model="form.isVisible" class="input">
            <option :value="1">显示</option>
            <option :value="0">隐藏</option>
          </select>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showForm = false">取消</button>
        <button class="btn btn-primary" @click="save">保存</button>
      </template>
    </BaseModal>

    <BaseModal :show="!!deleteTarget" title="确认删除" width="400px" @close="deleteTarget = null">
      <p>确定删除分类「{{ deleteTarget?.name }}」吗？</p>
      <template #footer>
        <button class="btn btn-ghost" @click="deleteTarget = null">取消</button>
        <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
      </template>
    </BaseModal>
  </div>
</template>

<style scoped>
@import '@/styles/admin-page.css';
.muted-cell {
  color: var(--muted);
  max-width: 320px;
}
.hidden-chip {
  background: #ece8df;
  color: var(--muted);
}
</style>
