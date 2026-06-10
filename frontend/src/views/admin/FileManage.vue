<script setup>
import { ref, onMounted } from 'vue'
import { adminListFiles, adminUploadFile, adminDeleteFile } from '@/api'
import { toast } from '@/utils/toast'
import { formatDate, formatSize } from '@/utils/format'
import BaseModal from '@/components/BaseModal.vue'

const list = ref([])
const loading = ref(true)
const uploading = ref(false)
const fileInput = ref(null)
const deleteTarget = ref(null)

async function load() {
  loading.value = true
  try {
    list.value = (await adminListFiles()) || []
  } catch (e) {
    toast.error(e.message)
  } finally {
    loading.value = false
  }
}

function triggerUpload() {
  fileInput.value?.click()
}

async function onFileChange(e) {
  const file = e.target.files?.[0]
  if (!file) return
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('file', file)
    await adminUploadFile(fd)
    toast.success('上传成功')
    await load()
  } catch (err) {
    toast.error(err.message)
  } finally {
    uploading.value = false
    e.target.value = ''
  }
}

function copyUrl(url) {
  const full = url
  navigator.clipboard?.writeText(full).then(
    () => toast.success('已复制链接'),
    () => toast.error('复制失败')
  )
}

async function confirmDelete() {
  try {
    await adminDeleteFile(deleteTarget.value.id)
    toast.success('删除成功')
    list.value = list.value.filter((f) => f.id !== deleteTarget.value.id)
  } catch (e) {
    toast.error(e.message)
  } finally {
    deleteTarget.value = null
  }
}

function isImage(type) {
  return ['png', 'jpg', 'jpeg', 'gif', 'webp', 'svg'].includes(
    (type || '').toLowerCase()
  )
}

onMounted(load)
</script>

<template>
  <div class="page">
    <div class="page-head">
      <div>
        <h1 class="page-title serif">文件管理</h1>
        <p class="page-sub">上传图片或附件，并在文章中引用其链接</p>
      </div>
      <button class="btn btn-primary" :disabled="uploading" @click="triggerUpload">
        {{ uploading ? '上传中…' : '+ 上传文件' }}
      </button>
      <input
        ref="fileInput"
        type="file"
        style="display: none"
        @change="onFileChange"
      />
    </div>

    <div class="card table-card">
      <div v-if="loading" class="state-box">正在加载…</div>
      <div v-else-if="list.length === 0" class="state-box">暂无文件，点击右上角上传</div>
      <table v-else class="data-table">
        <thead>
          <tr>
            <th>文件</th>
            <th>类型</th>
            <th>大小</th>
            <th>上传时间</th>
            <th style="text-align: right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="f in list" :key="f.id">
            <td>
              <div class="file-cell">
                <div class="file-thumb">
                  <img
                    v-if="isImage(f.fileType)"
                    :src="f.fileUrl"
                    :alt="f.fileName"
                  />
                  <span v-else class="file-ext">{{ (f.fileType || '?').toUpperCase() }}</span>
                </div>
                <span class="file-name">{{ f.fileName }}</span>
              </div>
            </td>
            <td>{{ f.fileType || '—' }}</td>
            <td>{{ formatSize(f.fileSize) }}</td>
            <td>{{ formatDate(f.uploadTime) }}</td>
            <td class="cell-actions">
              <button class="btn btn-ghost btn-sm" @click="copyUrl(f.fileUrl)">复制链接</button>
              <button class="btn btn-danger btn-sm" @click="deleteTarget = f">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BaseModal :show="!!deleteTarget" title="确认删除" width="400px" @close="deleteTarget = null">
      <p>确定删除文件「{{ deleteTarget?.fileName }}」吗？</p>
      <template #footer>
        <button class="btn btn-ghost" @click="deleteTarget = null">取消</button>
        <button class="btn btn-danger" @click="confirmDelete">确认删除</button>
      </template>
    </BaseModal>
  </div>
</template>

<style scoped>
@import '@/styles/admin-page.css';
.file-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}
.file-thumb {
  width: 42px;
  height: 42px;
  border-radius: 8px;
  background-color: var(--surface-2);
  border: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  flex-shrink: 0;
}
.file-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.file-ext {
  font-size: 11px;
  font-weight: 600;
  color: var(--muted);
}
.file-name {
  font-weight: 500;
  word-break: break-all;
}
</style>
