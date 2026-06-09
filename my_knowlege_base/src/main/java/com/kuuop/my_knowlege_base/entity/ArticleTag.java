package com.kuuop.my_knowlege_base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("kb_article_tag")
public class ArticleTag {
    private Long articleId;

    private Long tagId;
}
