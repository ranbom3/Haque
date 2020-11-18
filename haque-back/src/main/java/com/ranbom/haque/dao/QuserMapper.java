package com.ranbom.haque.dao;

import com.ranbom.haque.entity.Quser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ISheep
 * @create 2020/11/1 9:59
 */

@Mapper
public interface QuserMapper {
    /**
     *
     * @param username 用户名
     * @return 根据用户名查询一个用户
     */
    Quser loadUserByUsername(String username);

    /**
     *
     * @param quser 用户
     * @return 将用户数据存入数据库
     * 注册的时候默认状态为1：未激活，并且调用邮件服务发送激活码到邮箱
     */
    int saveToDb(Quser quser);

    /**
     * 点击邮箱中的激活码进行激活，根据激活码查询用户，之后再进行修改用户状态为1进行激活
     * @param code 邮箱中的激活码
     * @return Quser
     */
    Quser checkCode(String code);

    /**
     * 激活账户，修改用户状态为“1”进行激活
     * @param quser 用户
     */
    void updateUserStatus(Quser quser);

    void updateUserAvatar(int id, String imgUrl);

    Quser selectUserById(int id);

    Quser getEmail(String username);

    Integer updateUserPassword(String username,String password);

    void updateUserCode(Quser quser);
}
