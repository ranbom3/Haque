package com.ranbom.haque.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class Config implements WebMvcConfigurer {
    @Value("${file.upload.img.path}")
    private String locationPath;

    /**
     * 存放图片的URL路径，与本地路径相映射
     */
    private static final String NET_PATH = "/images/gallery/**";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(NET_PATH).addResourceLocations("file:///"+locationPath + "/");
    }
}