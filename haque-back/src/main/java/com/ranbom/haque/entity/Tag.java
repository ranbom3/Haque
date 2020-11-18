package com.ranbom.haque.entity;

import lombok.Data;

/**
 * @author dingj
 * @create 2020/11/11
 */

@Data
public class Tag {

    private int id;
    private String tagName;

    /**
     * 0代表未删除，1代表已删除
     */
    private int deleted;

}
