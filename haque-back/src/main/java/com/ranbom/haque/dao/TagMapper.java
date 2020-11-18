package com.ranbom.haque.dao;

import com.ranbom.haque.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/11
 */

@Mapper
public interface TagMapper {

    /**
     * 插入新的tag
     * @param tag 新的tag
     * @return 影响行数
     */
    int insertTag(Tag tag);

    /**
     * 查询所有tag
     * @return 所有tag的list
     */
    List<Tag> selectTags();

    /**
     * 根据文章ID和标签ID插入文章和标签的关系表
     * @param articleId 文章ID
     * @param tagId 标签ID
     * @return 影响行数
     */
    int insertArticleTags(int articleId, int tagId);
}
