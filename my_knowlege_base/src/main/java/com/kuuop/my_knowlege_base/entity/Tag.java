package com.kuuop.my_knowlege_base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String color;

    private LocalDateTime createTime;
}
