package com.gyh.Hamburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gyh.Hamburger.common.BaseContext;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.OrderDetail;
import com.gyh.Hamburger.domain.Orders;
import com.gyh.Hamburger.dto.OrdersDto;
import com.gyh.Hamburger.service.OrderDetailService;
import com.gyh.Hamburger.service.OrdersService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {

        log.info("订单信息:" + orders.toString());

        ordersService.submit(orders);
        return Result.success("已成功下单!");

    }


    //    http://localhost:8181/order/page?page=1&pageSize=10&number=11
    @GetMapping("/page")
    public Result<Page> showPage(int page, int pageSize, Long number,String beginTime,String endTime) {

        Page<Orders> ordersPage = new Page(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        if(beginTime!=null&&endTime!=null){
            queryWrapper.like(number != null, Orders::getNumber, number)
                    .gt(StringUtils.isEmpty(beginTime),Orders::getOrderTime,beginTime)
                    .lt(StringUtils.isEmpty(endTime),Orders::getOrderTime,endTime);
        }else{
            queryWrapper.like(number != null, Orders::getNumber, number);
        }


        ordersService.page(ordersPage, queryWrapper);
        return Result.success(ordersPage);
    }

    @GetMapping("/userPage")
    public Result<Page> page(int page, int pageSize){
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> pageDto = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //这里树直接把分页的全部结果查询出来，没有分页条件
        //添加排序条件，根据更新时间降序排列
//        查询当前用户的所有订单
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        this.ordersService.page(pageInfo,queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrderDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> orderDtoList = records.stream().map((item) ->{
            OrdersDto orderDto = new OrdersDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = this.ordersService.getOrderDetailsByOrderId(orderId);
            BeanUtils.copyProperties(item,orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //使用dto的分页有点难度.....需要重点掌握
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        pageDto.setRecords(orderDtoList);
        return Result.success(pageDto);
    }

}
