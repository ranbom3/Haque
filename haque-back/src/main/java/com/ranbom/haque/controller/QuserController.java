package com.ranbom.haque.controller;


import com.ranbom.haque.entity.Quser;
import com.ranbom.haque.entity.RespBean;
import com.ranbom.haque.service.QuserService;
import com.ranbom.haque.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;


/**
 * @author ISheep
 * @create 2020/11/1 10:30
 */

@Controller
public class QuserController {

    @Autowired
    QuserService quserService;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;
    @Value("${mymail.path}")
    private String mailPath;
    @Value("${old.path}")
    private String oldPath;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用户注册
     * @param quser 用户
     */
    @PostMapping("/register")
    @ResponseBody
    public RespBean reg(Quser quser) {
        String username = quser.getUsername();
        if (StringUtils.isBlank(username)) {
            return RespBean.error("用户名不能为空");
        }
        if (quserService.getQuserByUsername(username) != null) {
            return RespBean.error("用户名已存在");
        }
        String password = quser.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(password);
        quser.setAvatar(null);
        quser.setStatus(1);
        String code = UUIDUtils.getUUID();
        quser.setPassword(encodePassword);
        quser.setCode(code);
        quserService.reg(quser);
        return RespBean.ok("已将激活邮件发送至您的邮箱");
    }

    /**
     * 校验邮箱中的code激活账户
     * 首先根据激活码code查询用户，之后再把状态修改为"1"
     */
    @GetMapping("/checkCode")
    public String checkCode(String code) {
        Quser quser = quserService.checkCode(code);
        System.out.println(quser);
        // 如果用户不等于null，把用户状态修改status=0
        if (quser != null) {
            quser.setStatus(0);
            // 把code验证码清空，已经不需要了
            quser.setCode("");
            System.out.println(quser);
            quserService.updateUserStatus(quser);
            logger.info("激活成功");
        }
        return "activationSuccess";
    }

    /**
     * 
     * 根据忘记密码传来的code来修改密码
     */
    @GetMapping("/checkForgetCode")
    public String checkForgetCode(String code,Model model) {
        // TODO: 2020/11/9
        Quser quser = quserService.checkCode(code);
        System.out.println(quser);
        if (quser != null) {
            // 把code验证码清空，已经不需要了
            System.out.println(quser);
            quserService.updateUserCode(quser);
            model.addAttribute("quser", quser);
            return "setnewcode";
        }
        return "404";
    }

    // 忘记密码
    @PostMapping("/forget")
    @ResponseBody
    public RespBean forgetCode(String username) {
        Quser quser = quserService.getQuserByUsername(username);
        if (quser == null) {
            return RespBean.error("无此用户");
        }
        quser.setCode(UUIDUtils.getUUID());
        String code = quser.getCode();
        quserService.updateUserCode(quser);
        if (quser.getUsername() != null && quser.getStatus() == 0) {
            String subject = "来自Haque网站的激活忘记密码邮件";
            String url = oldPath + "/checkForgetCode?code=" + code;
            try {
                MimeMessage msg = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                helper.setSubject(subject);
                Context context = new Context();
                context.setVariable("username", quser.getUsername());
                context.setVariable("url", url);

                String process = templateEngine.process("forgetmail.html", context);
                helper.setText(process, true);
                helper.setFrom(from); //邮件的发送者
                helper.setSentDate(new Date());
                helper.setTo(quser.getEmail());
                javaMailSender.send(msg);
                logger.info("邮件已经发送。");
            } catch (MessagingException e) {
                logger.error("发送邮件时发生异常！", e);
            }
            return RespBean.ok("已将确认邮件发送至您的邮箱");
        }
        return RespBean.error("发生异常错误");
    }


    // 忘记密码后修改密码
    @PostMapping("/forgetAndUpdate")
    public String forgetAndUpdate(String username, String password) {
        Quser quser = quserService.getQuserByUsername(username);
        quser.setCode(null);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(password);
        quserService.updateUserCode(quser);
        quserService.updateUserPassword(username, encodePassword);
        logger.info("修改密码成功");
        return "login";
    }

    // 注册时判断用户名是否可用
    @GetMapping("/getUser")
    @ResponseBody
    public RespBean getUser(String username) {
        if (quserService.getQuserByUsername(username) == null) {
            return RespBean.ok("用户名可用");
        }
        return RespBean.error("用户名已存在");
    }
}
