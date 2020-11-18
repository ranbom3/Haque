package com.ranbom.haque.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ISheep
 * @create 2020/11/10 18:32
 * 配置路径映射
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/reg").setViewName("register");
        registry.addViewController("/loginsuccess").setViewName("loginsuccess");
    }
}
