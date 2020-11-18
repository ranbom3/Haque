package com.ranbom.haque.dao;

import com.ranbom.haque.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/12
 */

@Mapper
public interface QueryMapper {

    List<Article>selectArticlesByQuery(int quserId, String query, int limit, int offset);
}
