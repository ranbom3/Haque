package com.ranbom.haque.service;

import com.ranbom.haque.dao.QueryMapper;
import com.ranbom.haque.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/12
 */

@Service
public class QueryService {

    @Autowired
    private QueryMapper queryMapper;

    public List<Article> findArticlesByQuery(int quserId, String query, int limit, int offset) {
        return queryMapper.selectArticlesByQuery(quserId, query, limit, offset);
    }
}
