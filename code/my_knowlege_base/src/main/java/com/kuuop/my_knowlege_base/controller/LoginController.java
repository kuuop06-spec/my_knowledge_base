package com.kuuop.my_knowlege_base.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.dto.loginDto;
import com.kuuop.my_knowlege_base.entity.SysUser;
import com.kuuop.my_knowlege_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<String> login(@RequestBody loginDto dto) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            return Result.error("用户名或密码不能为空");
        }

        SysUser user = userService.getUserByUsername(dto.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        return Result.success(token);
    }
}
