-- 创建数据库
CREATE DATABASE IF NOT EXISTS `my_knowledge_base` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `my_knowledge_base`;

-- ----------------------------
-- 1. 管理员用户表 (sys_user)
-- ----------------------------
CREATE TABLE `sys_user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码(Bcrypt加密)',
  `nickname` VARCHAR(50) DEFAULT '管理员' COMMENT '前台显示的作者名',
  `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理员表';

-- ----------------------------
-- 2. 分类模块表 (kb_category) - 对应前台【上边栏】
-- ----------------------------
CREATE TABLE `kb_category` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `description` VARCHAR(255) COMMENT '分类描述(后台用)',
  `sort_order` INT DEFAULT 0 COMMENT '排序(数字越小越靠前)',
  `is_visible` TINYINT(1) DEFAULT 1 COMMENT '是否在前台显示: 1显示 0隐藏',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库分类表';

-- ----------------------------
-- 3. 标签表 (kb_tag) - 对应前台【右侧栏】
-- ----------------------------
CREATE TABLE `kb_tag` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
  `color` VARCHAR(20) DEFAULT '' COMMENT '预留字段：标签颜色(易拓展)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库标签表';

-- ----------------------------
-- 4. 文章核心表 (kb_article) - 对应前台【中间文章区】
-- ----------------------------
CREATE TABLE `kb_article` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(150) NOT NULL COMMENT '文章标题',
  `summary` VARCHAR(255) COMMENT '文章摘要(用于列表展示)',
  `content` LONGTEXT COMMENT '文章Markdown内容',
  `category_id` BIGINT NOT NULL COMMENT '所属分类ID',
  `view_count` INT DEFAULT 0 COMMENT '阅读量',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0草稿 1已发布',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建/发布时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文章表';

-- ----------------------------
-- 5. 文章-标签关联表 (kb_article_tag) - 多对多关系
-- ----------------------------
CREATE TABLE `kb_article_tag` (
  `article_id` BIGINT NOT NULL COMMENT '文章ID',
  `tag_id` BIGINT NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`article_id`, `tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- ----------------------------
-- 6. 文件/资源管理表 (kb_file) - 对应后台【文件管理】模块
-- ----------------------------
CREATE TABLE `kb_file` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `file_name` VARCHAR(150) NOT NULL COMMENT '文件原始名称',
  `file_url` VARCHAR(255) NOT NULL COMMENT '文件访问路径/URL',
  `file_type` VARCHAR(50) COMMENT '文件类型(pdf, docx, png, md等)',
  `file_size` BIGINT COMMENT '文件大小(字节)',
  `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件资源管理表';