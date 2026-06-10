package com.kuuop.my_knowlege_base.controller;

import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.entity.Tag;
import com.kuuop.my_knowlege_base.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {
    @Autowired
    private TagService tagService;

    // 前台：获取所有标签
    @GetMapping("/public/tags")
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.list());
    }

    // 后台：添加标签
    @PostMapping("/admin/tags")
    public Result<String> addTag(@RequestBody Tag tag) {
        boolean success = tagService.save(tag);
        return success ? Result.success("标签添加成功") : Result.error("标签添加失败");
    }

    // 后台：修改标签
    @PutMapping("/admin/tags/{id}")
    public Result<String> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        boolean success = tagService.updateById(tag);
        return success ? Result.success("标签修改成功") : Result.error("标签修改失败");
    }

    // 后台：删除标签
    @DeleteMapping("/admin/tags/{id}")
    public Result<String> deleteTag(@PathVariable Long id) {
        boolean success = tagService.removeById(id);
        return success ? Result.success("标签删除成功") : Result.error("标签删除失败");
    }
}
