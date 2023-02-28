package com.gyh.Hamburger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyh.Hamburger.domain.OrderDetail;
import com.gyh.Hamburger.domain.Orders;


import java.util.List;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
