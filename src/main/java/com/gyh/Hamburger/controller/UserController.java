package com.gyh.Hamburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gyh.Hamburger.common.EmailMessageUtil;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.User;
import com.gyh.Hamburger.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private UserService service;
    @Autowired
    EmailMessageUtil util;
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
//        获取邮箱
        String mail= user.getPhone();
//        获取随机的四位验证码
        String code= EmailMessageUtil.achieveCode();
        session.setAttribute(mail,code);
//        需要将生成的验证码保存到Redis中 并设置有效期为五分钟
        redisTemplate.opsForValue().set(mail,code,5, TimeUnit.MINUTES);

        util.sendMessage(mail,code);
//        调用阿里云（我调用不了）

        return Result.success("发送验证码成功");
    }

    @PostMapping("/login")
    public Result<User> sendMsg(@RequestBody Map map, HttpSession session){
//        获取邮箱
        log.info("map:{}",map);
//        获取验证码  获取邮箱
//        从session当中获取验证码

        String email = map.get("phone").toString();

        String code = (String) redisTemplate.opsForValue().get(email);



        String code1 = map.get("code").toString();


        if(code!=null&&code.equals(code1)){
//            比对成功
//            判断是否新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper= new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,email);
            User user =service.getOne(userLambdaQueryWrapper);
            if(user ==null){
                 user = new User();
                 user.setPhone(email);
                 user.setStatus(1);
                 service.save(user);
            }

            session.setAttribute("user",user.getId());
//            如果登录成功，将码删除掉
            redisTemplate.delete(email);
            return Result.success(user);
        }

        return Result.error("发送验证码成功");
    }
}
