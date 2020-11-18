package com.ranbom.haque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author dingj
 * @create 2020/11/9 11:52
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());

        // 设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        // 设置hash的key的序列化方式
        template.setValueSerializer(RedisSerializer.string());

        // 设置hash的value的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }
}
