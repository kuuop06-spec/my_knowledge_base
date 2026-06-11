<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">+ 新增文章</el-button>
      <el-input
        v-model="searchTitle"
        placeholder="搜索文章标题"
        style="width: 300px; margin-left: 20px"
        clearable
        @input="loadArticles"
      />
    </div>

    <el-table :data="tableData" stripe style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="250" />
      <el-table-column prop="categoryId" label="分类ID" width="100" />
      <el-table-column prop="viewCount" label="阅读量" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="deleteArticle(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="isEdit ? '编辑文章' : '新增文章'" v-model="dialogVisible" width="600px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="formData.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="formData.summary" placeholder="请输入文章摘要" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input-number v-model="formData.categoryId" :min="1" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="formData.content" placeholder="请输入文章内容（Markdown）" type="textarea" rows="6" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" placeholder="选择状态">
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveArticle">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const searchTitle = ref('')

const formData = ref({
  id: null,
  title: '',
  summary: '',
  categoryId: 1,
  content: '',
  status: 1,
  tagIds: []
})

const API_BASE = '/api'

// 加载文章列表
const loadArticles = async () => {
  try {
    const response = await axios.get(`${API_BASE}/public/articles`)
    if (response.data.code === 200) {
      let articles = response.data.data || []
      // 前端搜索过滤
      if (searchTitle.value) {
        articles = articles.filter(a => a.title.includes(searchTitle.value))
      }
      tableData.value = articles
    }
  } catch (error) {
    ElMessage.error('加载文章失败')
    console.error(error)
  }
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  formData.value = {
    id: null,
    title: '',
    summary: '',
    categoryId: 1,
    content: '',
    status: 1,
    tagIds: []
  }
  dialogVisible.value = true
}

// 显示编辑对话框
const showEditDialog = (row) => {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 保存文章
const saveArticle = async () => {
  try {
    if (!formData.value.title) {
      ElMessage.error('标题不能为空')
      return
    }

    const url = isEdit.value
      ? `${API_BASE}/admin/articles/${formData.value.id}`
      : `${API_BASE}/admin/articles`

    const method = isEdit.value ? 'put' : 'post'

    const response = await axios[method](url, {
      title: formData.value.title,
      summary: formData.value.summary,
      categoryId: formData.value.categoryId,
      content: formData.value.content,
      status: formData.value.status,
      tagIds: formData.value.tagIds
    })

    if (response.data.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      loadArticles()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error(error)
  }
}

// 删除文章
const deleteArticle = async (id) => {
  ElMessageBox.confirm('确定删除此文章吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const response = await axios.delete(`${API_BASE}/admin/articles/${id}`)
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          loadArticles()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        ElMessage.error('删除失败')
        console.error(error)
      }
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
</style>
