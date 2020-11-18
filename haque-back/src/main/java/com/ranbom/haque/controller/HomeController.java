package com.ranbom.haque.controller;

import com.ranbom.haque.dao.QueryMapper;
import com.ranbom.haque.entity.Article;
import com.ranbom.haque.entity.Page;
import com.ranbom.haque.entity.Quser;
import com.ranbom.haque.entity.Tag;
import com.ranbom.haque.service.ArticleService;
import com.ranbom.haque.service.QueryService;
import com.ranbom.haque.service.TagService;
import com.ranbom.haque.util.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private QueryService queryService;

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String getHomePage(Model model, Page page) {
        int quserId = 1;

        // 设置分页
        page.setLimit(4);
        page.setRows(articleService.findArticleRows(quserId));
        page.setPath("/");

        List<Article> articles = articleService.findArticles(quserId, page.getLimit(), page.getOffset());
        List<Article> latestArticlesList = articleService.findArticles(quserId, 3, 0);
        List<Map<String, Object>> articlesList = new ArrayList<>();

        if (articles != null) {
            for (Article article : articles) {
                Map<String, Object> map = new HashMap<>();
                // 将文章放入map
                map.put("article", article);
                // 获取文章的概要并存入map
                String articleResume = articleService.getArticleResume(article.getContent());
                map.put("articleResume", articleResume);
                // 获取文章对应的标签并存入map
                List<Tag> tags = articleService.findTagsByArticleId(article.getId());
                map.put("tags", tags);
                articlesList.add(map);
            }
        }
        if (articles.size() == 0) {
            model.addAttribute("indexMsg", "还未发布任何文章~");
        }

        List<Map<String, Integer>> archives = articleService.getArchive();
        List<Tag> tagList = tagService.findTags();

        model.addAttribute("articlesList", articlesList);
        model.addAttribute("latestArticlesList", latestArticlesList);
        model.addAttribute("archives", archives);
        model.addAttribute("tagList", tagList);
        return "index";
    }

    @GetMapping("about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("query")
    public String queryArticles(@RequestParam(value = "query", required = false) String query,
                                Model model) {
        int quserId = 1;

        if (StringUtils.isBlank(query)) {
            model.addAttribute("indexMsg", "请输入要搜索的文字~");
            return "query_result";
        }

        List<Article> articles = queryService.findArticlesByQuery(quserId, query, 100, 0);
        List<Map<String, Object>> articlesList = new ArrayList<>();

        if (articles != null) {
            for (Article article : articles) {
                Map<String, Object> map = new HashMap<>();
                // 将文章放入map
                map.put("article", article);
                // 获取文章的概要并存入map
                String articleResume = articleService.getArticleResume(article.getContent());
                map.put("articleResume", articleResume);
                // 获取文章对应的标签并存入map
                List<Tag> tags = articleService.findTagsByArticleId(article.getId());
                map.put("tags", tags);
                articlesList.add(map);
            }
        }
        if (articles.size() == 0) {
            model.addAttribute("indexMsg", "查询无结果~");
            model.addAttribute("queryKey", query);
            model.addAttribute("resultNum", 0);
            return "query_result";
        }
        model.addAttribute("articlesList", articlesList);
        model.addAttribute("queryKey", query);
        model.addAttribute("resultNum", articles.size());
        return "query_result";
    }

    @GetMapping("ipTest")
    @ResponseBody
    public String ipTest(HttpServletRequest request) {
        // 获取Ip地址
        return IpUtils.getIpAddr(request);
    }
}
