package com.ranbom.haque.service;

import com.ranbom.haque.dao.ArticleMapper;
import com.ranbom.haque.entity.Article;
import com.ranbom.haque.entity.Tag;
import com.ranbom.haque.util.GetResume;
import com.ranbom.haque.util.ParseMarkdown;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Service
public class ArticleService {

    private final static Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    /**
     *
     * @param quserId 用户ID
     * @param limit 查询数量
     * @param offset 查询偏移量
     * @return 某个用户的所有文章，以list形式返回
     */
    public List<Article> findArticles(int quserId, int limit, int offset) {
        List<Article> articles = articleMapper.selectArticles(quserId, limit, offset);
        return articles;
    }

    /**
     *
     * @param articleContent 文章内容
     * @return 返回文章内容概述
     */
    public String getArticleResume(String articleContent) {
        return GetResume.getResume(articleContent);
    }

    /**
     *
     * @param quserId 用户ID
     * @return 返回指定用户的发文数量
     */
    public int findArticleRows(int quserId) {
        return articleMapper.selectArticleRows(quserId);
    }

    /**
     *
     * @param id 文章ID
     * @return 返回指定ID的文章
     */
    public Article findArticleById(int id) {
        return articleMapper.selectArticleById(id);
    }

    public void addArticle(int id, String title, String markdown, int[] tags) {
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(markdown);

        Article article = new Article();
        article.setQuserId(id);
        article.setTitle(title);
        article.setContent(htmlContent);
        article.setMarkdown(markdown);
        article.setCreateTime(new Date());
        article.setStatus(0);

        articleMapper.insertArticle(article);

        // 将article和tag的关系插入到article_tag表中
        for (int tag : tags) {
            logger.info("ArticleService，正在插入article_tag表：{}-{}", article.getId(), tag);
            tagService.saveArticleTags(article.getId(), tag);
        }

    }

    public int deleteArticleById(int id) {
        return articleMapper.deleteArticleById(id);
    }

    public int modifyArticle(int articleId, String title, String markdown) {
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String content = pdp.markdownToHtml(markdown);
        return articleMapper.updateArticle(articleId, title, content, markdown);
    }

    public List<Map<String, Integer>> getArchive() {
        return articleMapper.selectArticlesByDate();
    }

    public List<Tag> findTagsByArticleId(int id) {
        Article article = articleMapper.selectArticleTagById(id);
        if (article != null) {
            return article.getTagList();
        }
        return null;
    }

}
