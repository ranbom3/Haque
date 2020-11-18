package com.ranbom.haque;


import com.ranbom.haque.dao.ArticleMapper;
import com.ranbom.haque.entity.Article;
import com.ranbom.haque.entity.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class ArticleMapperTests {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void selectArticlesTest() {
        List<Article> articles = articleMapper.selectArticles(1, 10, 0);
        for (Article article : articles) {
            System.out.println(article);
        }
    }

    @Test
    public void selectArticleByIdTest() {
        int articleId = 1;
        Article article = articleMapper.selectArticleById(articleId);
        if (article != null) {
            System.out.println(article);
        }
        else {
            System.out.println("此id的文章不存在！");
        }
    }

    @Test
    public void insertArticleTest() {
        Article article = new Article();
        article.setQuserId(1);
        article.setTitle("第四篇博客Title");
        article.setContent("第四篇博客Content");
        article.setCreateTime(new Date());
        article.setStatus(0);
        int res = articleMapper.insertArticle(article);
        System.out.println(res);
    }

    @Test
    public void selectArticleRowsTest() {
        int rows = articleMapper.selectArticleRows(1);
        System.out.println(rows);
    }

    @Test
    public void deleteArticleByIdTest() {
        int rows = articleMapper.deleteArticleById(13);
        System.out.println(rows);
    }

    @Test
    public void findArticlesByDateTest() {
        List<Map<String, Integer>> res = articleMapper.selectArticlesByDate();
        for (int i = 0; i < res.size(); i++) {
            Map<String, Integer> map = res.get(i);
            System.out.println(map);
        }
    }

    @Test
    public void findArticleTagsTest() {
        List<Article> articles = articleMapper.selectArticleTags();
        System.out.println(articles);
    }

    @Test
    public void findArticleTagByIdTest() {
        Article article = articleMapper.selectArticleTagById(41);
        System.out.println(article);
    }
}
