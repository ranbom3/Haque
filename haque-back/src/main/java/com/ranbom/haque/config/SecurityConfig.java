package com.ranbom.haque.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranbom.haque.entity.Quser;
import com.ranbom.haque.entity.RespBean;
import com.ranbom.haque.service.QuserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ISheep
 * @create 2020/10/31 13:29
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    QuserService quserService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(quserService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/fonts/**");
        web.ignoring().antMatchers("/admin/register/**");
        web.ignoring().antMatchers("/", "/article/detail/**");
        web.ignoring().antMatchers("/about");
        web.ignoring().antMatchers("/query");
        // 过滤注册的接口，防止出现注册失败
        web.ignoring().antMatchers("/reg/**", "/register", "/checkCode/**", "/forget",
                "/setnewcode", "/forgetAndUpdate/**", "/checkForgetCode/**", "/getUser", "/loginsuccess");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .successHandler(new AuthenticationSuccessHandler() {
                    // 登陆成功
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
                                                        Authentication authentication) throws IOException,
                            ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        Quser quser = (Quser) authentication.getPrincipal();
                        quser.setPassword(null);
                        RespBean ok = RespBean.ok("登陆成功", quser);
                        logger.info("登陆成功");
                        String s = new ObjectMapper().writeValueAsString(ok);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp,
                                                        AuthenticationException exception) throws IOException,
                            ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("登录失败！");
                        if (exception instanceof LockedException) {
                            respBean.setMsg("账户被锁定，请联系管理员!");
                        } else if (exception instanceof CredentialsExpiredException) {
                            respBean.setMsg("密码过期，请联系管理员!");
                        } else if (exception instanceof AccountExpiredException) {
                            respBean.setMsg("账户过期，请联系管理员");
                        } else if (exception instanceof DisabledException) {
                            respBean.setMsg("账户被禁用，请联系管理员");
                        } else if (exception instanceof BadCredentialsException) {
                            respBean.setMsg("用户名或密码输入错误，请重新输入");
                            logger.info("账户被锁定");
                        }
                        String s = new ObjectMapper().writeValueAsString(respBean);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")// 2
                .logoutSuccessUrl("/") // 3
                .invalidateHttpSession(true)  // 5
                .and()
                .csrf().disable();
        }
}
