package com.kuuop.my_knowlege_base.controller;

import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.entity.Category;
import com.kuuop.my_knowlege_base.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryContoller {
    @Autowired
    private CategoryService categoryService;

    // 前台：获取可见分类（用于导航栏）
    @GetMapping("/public/categories")
    public Result<List<Category>> getVisibleCategories() {
        return Result.success(categoryService.listVisible());
    }

    // 后台：获取所有分类
    @GetMapping("/admin/categories")
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.listAll());
    }

    // 后台：添加分类
    @PostMapping("/admin/categories")
    public Result<String> addCategory(@RequestBody Category category) {
        boolean success = categoryService.addCategory(category);
        return success ? Result.success("分类添加成功") : Result.error("分类添加失败");
    }

    // 后台：修改分类
    @PutMapping("/admin/categories/{id}")
    public Result<String> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        boolean success = categoryService.updateCategory(category);
        return success ? Result.success("分类修改成功") : Result.error("分类修改失败");
    }

    // 后台：删除分类
    @DeleteMapping("/admin/categories/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        boolean success = categoryService.deleteCategory(id);
        return success ? Result.success("分类删除成功") : Result.error("分类删除失败");
    }

    // 后台：获取单个分类
    @GetMapping("/admin/categories/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return category != null ? Result.success(category) : Result.error("分类不存在");
    }
}
