package com.ranbom.haque.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
public class Quser implements UserDetails, Serializable {

    private int id;
    private String username;
    private String password;
    private String email;
    private String avatar;
    /**
     * 状态：1代表未激活，0代表激活
     */
    private int status;
    /**
     * 利用UUID生成一段数字，发动到用户邮箱，当用户点击链接时
     * 在做一个校验如果用户传来的code跟我们发生的code一致，更改状态为“0”来激活用户
     */
    private String code;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
