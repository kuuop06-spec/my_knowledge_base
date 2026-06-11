# 知识库管理系统 - 快速启动指南

## 📋 项目结构

```
my_knowledge_base/          # 后端 Spring Boot 项目
kb-frontend/                # 前端 Vue 3 项目
```

---

## ⚙️ 后端启动步骤

### 1. 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS `my_knowledge_base` DEFAULT CHARACTER SET utf8mb4;
USE `my_knowledge_base`;
```

### 2. 执行建表 SQL
打开 MySQL 客户端，执行文件 `code/my_knowlege_base/ai_studio_code.sql` 中的所有 SQL 语句

### 3. 启动 Spring Boot 应用

进入后端项目目录：
```bash
cd code/my_knowlege_base
mvn clean spring-boot:run
```

或直接运行 `MyKnowlegeBaseApplication.java`

**验证后端运行**：
- 访问 http://localhost:8080/hello
- 应该看到响应（虽然内容不太雅观，但证明后端在跑）

---

## 🎨 前端启动步骤

### 1. 进入前端项目目录
```bash
cd kb-frontend
```

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
npm run dev
```

**前端应该在** http://localhost:5173 **运行**

---

## ✅ 功能检查清单

### 后端 API 列表（已实现）

#### 文章管理
- `GET  /api/public/articles` - 获取已发布文章
- `POST /api/admin/articles` - 新增文章
- `PUT  /api/admin/articles/{id}` - 修改文章
- `DELETE /api/admin/articles/{id}` - 删除文章

#### 分类管理
- `GET  /api/admin/categories` - 获取所有分类
- `POST /api/admin/categories` - 新增分类
- `PUT  /api/admin/categories/{id}` - 修改分类
- `DELETE /api/admin/categories/{id}` - 删除分类

#### 标签管理
- `GET  /api/public/tags` - 获取所有标签
- `POST /api/admin/tags` - 新增标签
- `POST /api/admin/tags/{id}` - 修改标签
- `DELETE /api/admin/tags/{id}` - 删除标签

### 前端功能（已实现）

- ✅ 文章管理：新增、编辑、删除、列表展示
- ✅ 分类管理：新增、编辑、删除、列表展示
- ✅ 标签管理：新增、编辑、删除、列表展示
- ✅ 响应式设计（Element Plus UI）
- ✅ 搜索功能（按标题搜索文章）

---

## 🐛 常见问题

### 问题 1：前端无法连接后端 API
**解决**：确保：
- 后端在 http://localhost:8080 运行
- Vite 代理配置正确（`vite.config.js` 中已配置）
- 浏览器开发者工具 Console 检查报错信息

### 问题 2：MySQL 连接失败
**检查**：
- MySQL 服务是否启动
- `application.yaml` 中的数据库密码是否正确（当前为 `88888888`）
- 数据库 `my_knowledge_base` 是否已创建

### 问题 3：前端 npm install 失败
**尝试**：
```bash
npm install --legacy-peer-deps
```

---

## 📦 打包部署

### 构建前端
```bash
cd kb-frontend
npm run build
```

生成的 `dist/` 文件夹可以部署到任何 web 服务器

### 构建后端
```bash
cd code/my_knowlege_base
mvn clean package
```

生成的 JAR 文件在 `target/` 下

---

## 💡 后续优化建议（提交后可做）

1. **前端路由**：加入 Vue Router，分离前台和后台
2. **权限鉴权**：后台接口添加 JWT 认证
3. **文件上传**：完善 FileController 的上传功能
4. **分页**：表格数据分页展示
5. **统计看板**：添加仪表盘，显示文章数、分类数等统计信息

---

## 🎯 提交清单

交作业时，确保提交：
- [ ] 后端代码（`code/my_knowlege_base/`）
- [ ] 前端代码（`kb-frontend/`）
- [ ] `package.json` 和 `pom.xml`（依赖声明）
- [ ] README.md（说明文档）
- [ ] 数据库 SQL 脚本（`ai_studio_code.sql`）
