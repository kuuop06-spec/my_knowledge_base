package com.kuuop.my_knowlege_base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuuop.my_knowlege_base.entity.Article;
import com.kuuop.my_knowlege_base.entity.ArticleTag;
import com.kuuop.my_knowlege_base.mapper.ArticleMapper;
import com.kuuop.my_knowlege_base.mapper.ArticleTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    // 获取所有已发布文章
    public List<Article> listPublished() {
        return this.list(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime));
    }

    // 按分类获取已发布文章
    public List<Article> listByCategoryId(Long categoryId) {
        return this.list(new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, categoryId)
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime));
    }

    // 增加阅读量
    public void increaseViewCount(Long articleId) {
        Article article = this.getById(articleId);
        if (article != null) {
            article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);
            this.updateById(article);
        }
    }

    // 发布文章（同时处理标签关联）
    public boolean publishArticle(Article article, List<Long> tagIds) {
        boolean saved = this.save(article);
        if (saved && tagIds != null && !tagIds.isEmpty()) {
            // 保存文章-标签关联关系
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            }
        }
        return saved;
    }

    // 更新文章（同时更新标签）
    public boolean updateArticleWithTags(Article article, List<Long> tagIds) {
        this.updateById(article);

        // 删除原有标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, article.getId()));

        // 重新关联新标签
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagId);
                articleTagMapper.insert(articleTag);
            }
        }
        return true;
    }
}
