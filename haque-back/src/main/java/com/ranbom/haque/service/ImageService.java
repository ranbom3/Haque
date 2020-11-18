package com.ranbom.haque.service;

import com.ranbom.haque.dao.ImageMapper;
import com.ranbom.haque.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public int addImage(String imgUrl) {
        Image image = new Image();
        image.setImgUrl(imgUrl);
        return imageMapper.insertImg(image);
    }

    public List<Image> findImages() {
        return imageMapper.selectAllImages();
    }

    public int deleteImage(String imgUrl) {
        return imageMapper.deleteImg(imgUrl);
    }
}
