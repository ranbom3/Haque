package com.ranbom.haque;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class RedisTests {

    private final static Logger logger = LoggerFactory.getLogger(RedisTests.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest() {
        String redisKey = "test:hello";
        redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void pvTest() {
        String redisKey = "article1";
        String ip1 = "192.168.0.1";
//        redisTemplate.opsForHash().put(redisKey, ip1, 0);
        int num = (int)redisTemplate.opsForHash().get(redisKey, ip1);
        System.out.println(num);
        num = num + 1;
        redisTemplate.opsForHash().put(redisKey, ip1, num);
        System.out.println(redisTemplate.opsForHash().get(redisKey, ip1));
    }

    /**
     * 统计所有文章
     */
    @Test
    public void allArticlesTest() {

        Set keys = redisTemplate.keys("*");
        System.out.println(keys);
    }

    /**
     * 获得单篇文章的访问量，ip地址：访问量
     * 统计pv,uv
     */
    @Test
    public void singleArticleTest() {
        String redisKey = "article:23:pv";
        Map map = redisTemplate.opsForHash().entries(redisKey);
        logger.info(String.valueOf(map));

        int uv = map.size();
        int pv = 0;

        Set set = map.entrySet();
        System.out.println(set);

//        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
//            logger.info("key:{}, value:{}", entry.getKey(), entry.getValue());
////            pv += entry.getValue();
//        }

        for (Object key : map.keySet()) {
            pv += (int)map.get(key);
        }
        logger.info("pv:{}", pv);
        logger.info("uv:{}", uv);
    }




}
