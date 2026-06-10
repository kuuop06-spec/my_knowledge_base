package com.kuuop.my_knowlege_base.controller;

import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.entity.Tag;
import com.kuuop.my_knowlege_base.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;


//todo:标签是用不是用来分类文章的，做推荐算法使用

@RestController
@RequestMapping("/api")
public class TagController {
    @Autowired
    private TagService tagService;

    // 前台：获取所有标签
    @GetMapping("public/tags")
    public Result<List<Tag>> getAallTags() {
        return Result.success(tagService.list());
    }

    //添加标签
    @PostMapping("/admin/tags")
    public Result<String> addTags(@RequestBody Tag tag){
        boolean issuccess = tagService.save(tag);
        return issuccess?Result.success("标签添加成功"):Result.error("标签添加失败");
    }

    // 后台：修改标签
    //Q：这里传id，防止用户改id，标签不能动
    @PostMapping("/admin/tags/{id}")
    public Result<String> updateTags(@PathVariable Long id,@RequestBody Tag tag){
        tag.setId(id);
        final boolean issuccess = tagService.updateById(tag);
        return issuccess?Result.success("标签修改成功"):Result.error("标签修改失败");
    }

    // 后台：删除标签
    //todo:删除标签时，需要判断标签下是否有文章，如果有文章，则先删除关联表中的记录和，再删除标签本身;
    @DeleteMapping("/admin/tags/{id}")
    public Result<String> deleteTag(@PathVariable Long id) {
        final boolean iss = tagService.removeById(id);
        return iss?Result.success("标签删除成功"):Result.error("标签删除失败");
    }
}
