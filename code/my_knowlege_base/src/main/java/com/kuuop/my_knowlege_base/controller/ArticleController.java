package com.kuuop.my_knowlege_base.controller;

import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.dto.ArticlePublishDto;
import com.kuuop.my_knowlege_base.entity.Article;
import com.kuuop.my_knowlege_base.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // 前台：获取所有已发布文章
    @GetMapping("/public/articles")
    public Result<List<Article>> getPublishedArticles() {
        return Result.success(articleService.listPublished());
    }

    // 前台：按分类获取文章
    @GetMapping("/public/articles/category/{categoryId}")
    public Result<List<Article>> getArticlesByCategory(@PathVariable Long categoryId) {
        return Result.success(articleService.listByCategoryId(categoryId));
    }

    // 前台：获取文章详情（同时增加阅读量）
    @GetMapping("/public/articles/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article != null) {
            articleService.increaseViewCount(id);
            return Result.success(article);
        }
        return Result.error("文章不存在");
    }

    // 后台：发布文章
    @PostMapping("/admin/articles")
    public Result<String> publishArticle(@RequestBody ArticlePublishDto dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCategoryId(dto.getCategoryId());
        article.setStatus((byte) 1);  // 1=已发布
        article.setViewCount(0);

        boolean success = articleService.publishArticle(article, dto.getTagIds());
        return success ? Result.success("文章发布成功") : Result.error("文章发布失败");
    }

    // 后台：修改文章
    @PutMapping("/admin/articles/{id}")
    public Result<String> updateArticle(@PathVariable Long id, @RequestBody ArticlePublishDto dto) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }

        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCategoryId(dto.getCategoryId());

        boolean success = articleService.updateArticleWithTags(article, dto.getTagIds());
        return success ? Result.success("文章修改成功") : Result.error("文章修改失败");
    }

    // 后台：上线/下线文章
    @PutMapping("/admin/articles/{id}/status/{status}")
    public Result<String> updateArticleStatus(@PathVariable Long id, @PathVariable Byte status) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        article.setStatus(status);
        boolean success = articleService.updateById(article);
        return success ? Result.success("文章状态修改成功") : Result.error("文章状态修改失败");
    }

    // 后台：删除文章
    @DeleteMapping("/admin/articles/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        boolean success = articleService.removeById(id);
        return success ? Result.success("文章删除成功") : Result.error("文章删除失败");
    }
}
