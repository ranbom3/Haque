package com.ranbom.haque;

import com.ranbom.haque.dao.QuserMapper;
import com.ranbom.haque.entity.Quser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ISheep
 * @create 2020/11/1 10:06
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class QuserMapperTests {

    @Autowired
    QuserMapper quserMapper;

    @Test
    public void test(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }
}
