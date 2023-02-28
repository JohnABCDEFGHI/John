package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gyh.Hamburger.domain.OrderDetail;
import com.gyh.Hamburger.mapper.OrderDetailMapper;
import com.gyh.Hamburger.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
     implements OrderDetailService {
}
