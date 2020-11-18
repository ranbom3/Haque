package com.ranbom.haque.controller;

import com.ranbom.haque.entity.Article;
import com.ranbom.haque.entity.Tag;
import com.ranbom.haque.service.ArticleService;
import com.ranbom.haque.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Controller
@RequestMapping(path = "/article")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/detail/{articleId}")
    public String getArticleDetailPage(@PathVariable("articleId") int articleId,
                                       Model model,
                                       HttpServletRequest request) {
        // 获取文章
        Article article = articleService.findArticleById(articleId);
        model.addAttribute("article", article);

        // 获取IP地址
        String ipAddress = IpUtils.getIpAddr(request);
        logger.info("ip：{}的用户访问了标题：{}的博客", ipAddress, article.getTitle());


        Object tmp = redisTemplate.opsForHash().get("article:"+article.getId()+":pv", ipAddress);
        if (tmp == null) {
            // 此ip第一次访问此文章，将value设置为1
            redisTemplate.opsForHash().put("article:"+article.getId()+":pv", ipAddress, 1);
            logger.info("ip：{}的用户，第1次访问了标题：{}的博客", ipAddress, article.getTitle());
        }
        else {
            int num = (int)tmp;
            // 此篇文章的访问量+1
            num += 1;
            redisTemplate.opsForHash().put("article:"+article.getId()+":pv", ipAddress, num);
            logger.info("ip：{}的用户，第{}次访问了标题：{}的博客", ipAddress, num, article.getTitle());
        }


        Map map = redisTemplate.opsForHash().entries("article:"+article.getId()+":pv");
        // uv，同一个ip多次访问，算一个
        int uv = map.size();
        model.addAttribute("uv", uv);
        logger.info("标题：{}的博客，访问量(uv)：{}", article.getTitle(), uv);

        // pv，忽略ip地址，统计所有访问量
        int pv = 0;
        for (Object key : map.keySet()) {
            pv += (int)map.get(key);
        }

        model.addAttribute("pv", pv);
        logger.info("标题：{}的博客，访问量(pv)：{}", article.getTitle(), pv);

        // 获取标签
        List<Tag> tags = articleService.findTagsByArticleId(article.getId());
        model.addAttribute("tags", tags);
        logger.info("此文章的tags:{}", tags);

        return "post";
    }
}
