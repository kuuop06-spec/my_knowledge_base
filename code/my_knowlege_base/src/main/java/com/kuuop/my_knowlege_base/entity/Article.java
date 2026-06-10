package com.kuuop.my_knowlege_base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String summary;

    private String content;

    private Long categoryId;

    private Integer viewCount;

    private Byte status;  // 0=草稿, 1=已发布

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
