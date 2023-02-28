package com.gyh.Hamburger.dto;

import com.gyh.Hamburger.domain.OrderDetail;
import com.gyh.Hamburger.domain.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
