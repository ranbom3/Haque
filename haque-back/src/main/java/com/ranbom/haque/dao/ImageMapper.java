package com.ranbom.haque.dao;

import com.ranbom.haque.entity.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Mapper
public interface ImageMapper {

    int insertImg(Image image);

    List<Image> selectAllImages();

    int deleteImg(String imgUrl);
}
