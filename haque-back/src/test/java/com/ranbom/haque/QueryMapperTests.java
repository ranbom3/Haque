package com.ranbom.haque;

import com.ranbom.haque.dao.QueryMapper;
import com.ranbom.haque.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/12
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class QueryMapperTests {

    private final static Logger logger = LoggerFactory.getLogger(QueryMapperTests.class);

    @Autowired
    private QueryMapper queryMapper;

    @Test
    public void selectArticlesByQueryTest() {
        List<Article> articles = queryMapper.selectArticlesByQuery(1, "博客", 2, 0);
        logger.info(String.valueOf(articles));
    }
}
