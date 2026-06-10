package com.kuuop.my_knowlege_base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuuop.my_knowlege_base.entity.SysUser;
import com.kuuop.my_knowlege_base.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<SysUserMapper, SysUser> {

    public SysUser getUserByUsername(String username) {
        return this.getOne(new QueryWrapper<SysUser>().eq("username", username));
    }
}
