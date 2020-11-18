package com.ranbom.haque.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Article {

    private int id;
    private int quserId;
    private String title;
    private String content;
    private String markdown;
    private Date createTime;
    private int status;

    /**
     * 用来存放文章的的标签
     */
    private List<Tag> tagList;
}
