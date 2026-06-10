package com.kuuop.my_knowlege_base.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticlePublishDto {
    private String title;
    private String summary;
    private String content;
    private Long categoryId;
    private List<Long> tagIds;  // 标签ID列表
}
