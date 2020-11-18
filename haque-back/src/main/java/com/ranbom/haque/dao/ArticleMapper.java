package com.ranbom.haque.dao;

import com.ranbom.haque.entity.Article;
import com.ranbom.haque.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Mapper
public interface ArticleMapper {

    /**
     * 根据分页信息返回所有文章
     * @param quserId 用户ID
     * @param limit 查询数量
     * @param offset 查询偏移量
     * @return 某个用户的所有文章，以list形式返回
     */
    List<Article> selectArticles(int quserId, int limit, int offset);

    /**
     * 根据文章ID查询某个文章的数据
     * @param id 文章ID
     * @return 具体的某一篇文章
     */
    Article selectArticleById(int id);

    /**
     * 插入文章
     * @param article 文章对象
     * @return 影响行数
     */
    int insertArticle(Article article);

    /**
     * 查看某用户文章数量
     * @param quserId 用户ID
     * @return 文章数量
     */
    int selectArticleRows(int quserId);

    /**
     * 根据ID删除文章
     * @param id 文章ID
     * @return 影响行数
     */
    int deleteArticleById(int id);

    /**
     * 根据ID更新文章
     * @param id 文章ID
     * @param title 文章标题
     * @param content 文章内容
     * @param markdown 文章markdown格式内容
     * @return 影响行数
     */
    int updateArticle(int id, String title, String content, String markdown);

    /**
     * 根据日期统计每个月文章数量
     * @return Map->月份：文章数量
     */
    List<Map<String, Integer>> selectArticlesByDate();

    /**
     * 查出所有的文章及标签
     * @return 标签的list
     */
    List<Article> selectArticleTags();

    Article selectArticleTagById(int id);

}
