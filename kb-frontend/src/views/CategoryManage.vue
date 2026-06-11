<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="showAddDialog">+ 新增分类</el-button>
    </div>

    <el-table :data="tableData" stripe style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" width="200" />
      <el-table-column prop="description" label="描述" min-width="250" />
      <el-table-column prop="sortOrder" label="排序" width="100" />
      <el-table-column prop="isVisible" label="是否显示" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isVisible === 1 ? 'success' : 'info'">
            {{ row.isVisible === 1 ? '显示' : '隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="deleteCategory(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="isEdit ? '编辑分类' : '新增分类'" v-model="dialogVisible" width="500px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="分类名称">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="formData.description" placeholder="请输入分类描述" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="是否显示">
          <el-select v-model="formData.isVisible" placeholder="选择是否显示">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">保存</el-button>
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
  description: '',
  sortOrder: 0,
  isVisible: 1,
  parentId: null
})

const API_BASE = '/api'

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await axios.get(`${API_BASE}/admin/categories`)
    if (response.data.code === 200) {
      tableData.value = response.data.data || []
    }
  } catch (error) {
    ElMessage.error('加载分类失败')
    console.error(error)
  }
}

// 显示新增对话框
const showAddDialog = () => {
  isEdit.value = false
  formData.value = {
    id: null,
    name: '',
    description: '',
    sortOrder: 0,
    isVisible: 1,
    parentId: null
  }
  dialogVisible.value = true
}

// 显示编辑对话框
const showEditDialog = (row) => {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 保存分类
const saveCategory = async () => {
  try {
    if (!formData.value.name) {
      ElMessage.error('分类名称不能为空')
      return
    }

    const url = isEdit.value
      ? `${API_BASE}/admin/categories/${formData.value.id}`
      : `${API_BASE}/admin/categories`

    const method = isEdit.value ? 'put' : 'post'

    const response = await axios[method](url, {
      name: formData.value.name,
      description: formData.value.description,
      sortOrder: formData.value.sortOrder,
      isVisible: formData.value.isVisible,
      parentId: formData.value.parentId
    })

    if (response.data.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      loadCategories()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error(error)
  }
}

// 删除分类
const deleteCategory = async (id) => {
  ElMessageBox.confirm('确定删除此分类吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const response = await axios.delete(`${API_BASE}/admin/categories/${id}`)
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          loadCategories()
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
  loadCategories()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
</style>
