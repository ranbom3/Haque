package com.ranbom.haque;

import com.ranbom.haque.dao.TagMapper;
import com.ranbom.haque.entity.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingj
 * @create 2020/11/11
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class TagMapperTests {

    @Autowired
    private TagMapper tagMapper;

    @Test
    public void insertTagTest() {
        Tag tag = new Tag();
        tag.setTagName("随笔");
        tag.setDeleted(0);
        System.out.println(tagMapper.insertTag(tag));
    }

    @Test
    public void selectTagsTest() {
        List<Tag> tags = new ArrayList<>();
        tags = tagMapper.selectTags();
        System.out.println(tags);
    }


}
