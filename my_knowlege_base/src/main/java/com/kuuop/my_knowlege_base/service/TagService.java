package com.kuuop.my_knowlege_base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuuop.my_knowlege_base.entity.Tag;
import com.kuuop.my_knowlege_base.mapper.TagMapper;
import org.springframework.stereotype.Service;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> {
}
