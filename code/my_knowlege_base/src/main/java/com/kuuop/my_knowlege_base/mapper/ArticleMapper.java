package com.kuuop.my_knowlege_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuuop.my_knowlege_base.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
