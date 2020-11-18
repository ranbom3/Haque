package com.ranbom.haque.util;

import java.util.UUID;

/**
 * @author ISheep
 * @create 2020/11/5 18:33
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}