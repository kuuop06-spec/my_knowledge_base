package com.kuuop.my_knowlege_base.config;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 指定一条 match 规则
            SaRouter
                    .match("/api/**")              // 拦截 /api/ 下的所有请求
                    .notMatch("/api/auth/register") // 排除注册接口
                    .notMatch("api/public/**")
                    .notMatch("/api/auth/login")   // 排除登录接口
                    .notMatch("/api/article/list") // 排除前台文章列表(游客可看)
                    .notMatch("/api/article/detail/**") // 排除文章详情
                    .check(r -> StpUtil.checkLogin()); // 执行鉴权：必须登录
        })).addPathPatterns("/**");
    }
}
