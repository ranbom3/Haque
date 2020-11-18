package com.ranbom.haque.service;

import com.ranbom.haque.dao.TagMapper;
import com.ranbom.haque.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/11
 */

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> findTags() {
        return tagMapper.selectTags();
    }

    public int saveArticleTags(int articleId, int tagId) {
        return tagMapper.insertArticleTags(articleId, tagId);
    }

    public void saveNewTag(String tagName) {
        Tag tag = new Tag();
        tag.setTagName(tagName);
        tag.setDeleted(0);
        tagMapper.insertTag(tag);
    }
}
