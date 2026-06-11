<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">+ 新增标签</el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="标签名称" width="200" />
      <el-table-column prop="color" label="颜色" width="150" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="deleteTag(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="isEdit ? '编辑标签' : '新增标签'" v-model="dialogVisible" width="400px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="标签名称">
          <el-input v-model="formData.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="颜色">
          <el-input v-model="formData.color" placeholder="如：#ff0000 或 red" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTag">保存</el-button>
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

const formData = ref({
  id: null,
  name: '',
  color: ''
})

const API_BASE = '/api'

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await axios.get(`${API_BASE}/public/tags`)
    if (response.data.code === 200) {
      tableData.value = response.data.data || []
    }
  } catch (error) {
    ElMessage.error('加载标签失败')
    console.error(error)
  }
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  formData.value = {
    id: null,
    name: '',
    color: ''
  }
  dialogVisible.value = true
}

// 显示编辑对话框
const showEditDialog = (row) => {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 保存标签
const saveTag = async () => {
  try {
    if (!formData.value.name) {
      ElMessage.error('标签名称不能为空')
      return
    }

    const url = isEdit.value
      ? `${API_BASE}/admin/tags/${formData.value.id}`
      : `${API_BASE}/admin/tags`

    const method = isEdit.value ? 'post' : 'post'

    const response = await axios[method](url, {
      name: formData.value.name,
      color: formData.value.color
    })

    if (response.data.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      loadTags()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error(error)
  }
}

// 删除标签
const deleteTag = async (id) => {
  ElMessageBox.confirm('确定删除此标签吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const response = await axios.delete(`${API_BASE}/admin/tags/${id}`)
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          loadTags()
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
  loadTags()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
</style>
