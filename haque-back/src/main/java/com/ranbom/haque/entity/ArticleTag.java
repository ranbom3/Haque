package com.ranbom.haque.entity;

import lombok.Data;

/**
 * @author dingj
 * @create 2020/11/11
 */

@Data
public class ArticleTag {

    private int id;
    private Article article;
    private Tag tag;

}
