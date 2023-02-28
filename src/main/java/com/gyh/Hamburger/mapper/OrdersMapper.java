package com.gyh.Hamburger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.gyh.Hamburger.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
