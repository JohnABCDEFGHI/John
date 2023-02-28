package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.domain.User;
import com.gyh.Hamburger.mapper.UserMapper;
import com.gyh.Hamburger.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
}
