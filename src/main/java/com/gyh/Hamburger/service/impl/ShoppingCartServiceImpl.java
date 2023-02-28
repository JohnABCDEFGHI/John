package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gyh.Hamburger.domain.ShoppingCart;
import com.gyh.Hamburger.mapper.ShoppingCartMapper;
import com.gyh.Hamburger.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
     implements ShoppingCartService {
}
