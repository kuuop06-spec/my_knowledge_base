package com.kuuop.my_knowlege_base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuuop.my_knowlege_base.entity.Category;
import com.kuuop.my_knowlege_base.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    // 获取所有分类（已排序）
    public List<Category> listAll() {
        return this.list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSortOrder));
    }

    // 获取前台可见的分类
    public List<Category> listVisible() {
        return this.list(new LambdaQueryWrapper<Category>()
                .eq(Category::getIsVisible, 1)
                .orderByAsc(Category::getSortOrder));
    }

    // 添加分类
    public boolean addCategory(Category category) {
        return this.save(category);
    }

    // 修改分类
    public boolean updateCategory(Category category) {
        return this.updateById(category);
    }

    // 删除分类
    public boolean deleteCategory(Long id) {
        return this.removeById(id);
    }

    // 获取单个分类
    public Category getById(Long id) {
        return this.getById(id);
    }
}
