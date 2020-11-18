package com.ranbom.haque.service;

import com.ranbom.haque.dao.QuserMapper;
import com.ranbom.haque.entity.Quser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ISheep
 * @create 2020/11/1 9:58
 */

@Service
public class QuserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 配置文件中的qq邮箱
     */
    @Value("${spring.mail.username}")
    private String from;
    @Value("${mymail.path}")
    private String mailPath;
    @Value("${old.path}")
    private String oldPath;

    @Autowired
    QuserMapper quserMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Quser quser = quserMapper.loadUserByUsername(username);
        if (quser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return quser;
    }

    /**
     * 用户注册，同时发送一封激活邮件
     * @param quser 用户
     */
    public void reg(Quser quser) {
        quserMapper.saveToDb(quser);
        // //获取激活码
        String code = quser.getCode();
        System.out.println("code:" + code);
        // 主题
        String subject = "来自Haque网站的激活邮件";
        // checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“0”激活
        // 上面的激活码发送到用户注册邮箱
        String url = oldPath + "/checkCode?code=" + code;
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariable("username", quser.getUsername());
            context.setVariable("url", url);
            context.setVariable("time", sdf.format(new Date()));
            String process = templateEngine.process("mail.html", context);
            helper.setText(process, true);
            helper.setFrom(from); //邮件的发送者
            helper.setSentDate(new Date());
            helper.setTo(quser.getEmail());

            javaMailSender.send(msg);
            logger.info("邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送邮件时发生异常！", e);
        }
    }

    /**
     *
     * @param code 激活码
     * @return Quser 用户
     */
    public Quser checkCode(String code) {
        return quserMapper.checkCode(code);
    }

    /**
     * 激活账户，修改用户状态
     * @param quser 用户
     */
    public void updateUserStatus(Quser quser) {
        quserMapper.updateUserStatus(quser);
    }

    public void updateUserAvatar(int quserId, String imgUrl) {
        quserMapper.updateUserAvatar(quserId, imgUrl);
    }

    public Quser findUserById(int quserId) {
        return quserMapper.selectUserById(quserId);
    }

    public Quser getQuserByUsername(String username) {
        return quserMapper.loadUserByUsername(username);
    }

    public Integer updateUserPassword(String username, String password) {
        return quserMapper.updateUserPassword(username, password);
    }

    public void updateUserCode(Quser quser) {
        quserMapper.updateUserCode(quser);
    }
}
