<template>
  <div class="app-container">
    <el-container>
      <el-header class="header">
        <div class="header-title">📚 知识库管理系统</div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu :default-active="activeTab" @select="activeTab = $event">
            <el-menu-item index="article">文章管理</el-menu-item>
            <el-menu-item index="category">分类管理</el-menu-item>
            <el-menu-item index="tag">标签管理</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main">
          <ArticleManage v-if="activeTab === 'article'" />
          <CategoryManage v-if="activeTab === 'category'" />
          <TagManage v-if="activeTab === 'tag'" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ArticleManage from './views/ArticleManage.vue'
import CategoryManage from './views/CategoryManage.vue'
import TagManage from './views/TagManage.vue'

const activeTab = ref('article')
</script>

<style scoped>
.app-container {
  height: 100vh;
}

.header {
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.header-title {
  font-size: 24px;
  font-weight: bold;
}

.aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e6e6e6;
}

.main {
  padding: 20px;
}

:deep(.el-menu) {
  border-right: none;
}
</style>
