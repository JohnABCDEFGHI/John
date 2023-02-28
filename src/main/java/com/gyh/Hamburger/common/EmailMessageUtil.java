package com.gyh.Hamburger.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class EmailMessageUtil{

    @Autowired
    JavaMailSenderImpl mailSender;

    @Value("${check.mail.sender}")
    private String sender;

    @Value("${check.mail.subject}")
    private String subject;

    @Value("${check.mail.str1}")
    private String str1;

    @Value("${check.mail.str2}")
    private String str2;
    /**
     * 发送邮件
     *
     * @return 提示信息
     */
    public String sendMessage(String email,String code)throws MailException {
        //引入编码工具类
        SimpleMailMessage message = new SimpleMailMessage();
        //发送者
        message.setFrom(sender);
        //发送邮件地址
        message.setTo(email);
        //标题
        message.setSubject(subject);
        //内容
        message.setText(str1+code+str2);
        mailSender.send(message);
        return "send success";
    }

    // 随机验证码
    public static String achieveCode() {
        String[] beforeShuffle= new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
        System.out.print(result);
        return result;
    }


}
