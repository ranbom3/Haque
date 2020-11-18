package com.ranbom.haque;

import com.ranbom.haque.util.UUIDUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ISheep
 * @create 2020/11/5 18:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HaqueApplication.class)
public class ISheepTests {

    @Value("${mymail.path}")
    private String mailPath;
    
    @Test
    public void test(){
        String s = null;
        System.out.println(UUIDUtils.getUUID());
    }

    @Test
    public void test2(){
        String url = mailPath + "/checkCode?code=";
        System.out.println(url);
        
    }
}
