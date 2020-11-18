package com.ranbom.haque;


import com.ranbom.haque.dao.ImageMapper;
import com.ranbom.haque.entity.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class ImageMapperTests {

    @Autowired
    private ImageMapper imageMapper;

    @Test
    public void insertImg() {
        Image image = new Image();
        image.setImgUrl("\\images\\1.png");
        imageMapper.insertImg(image);
    }
}
