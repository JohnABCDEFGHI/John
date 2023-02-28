package com.gyh.Hamburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gyh.Hamburger.common.BaseContext;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.ShoppingCart;
import com.gyh.Hamburger.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//http://localhost:8080/shoppingCart/list
@RestController
@Slf4j

@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCarts = service.list(queryWrapper);

        return Result.success(shoppingCarts);
    }

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart cart){
        log.info("cart:{}",cart);

//        设置用户ID 指定当前是哪个用户的数据

        Long current_ID = BaseContext.getCurrentId();
        cart.setUserId(current_ID);

//        如果同一个菜品点了两份  数量就加一
        Long dishId= cart.getDishId();
//          构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper= new LambdaQueryWrapper<>();
        if(dishId!=null){
//添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }


//        查询一下添加的菜品或者套餐是否有没有在购物车


        ShoppingCart serviceOne = service.getOne(queryWrapper);

//        如果查出来就说明 已经有了 数量加一

        if(serviceOne!=null){

            Integer number = serviceOne.getNumber();
            serviceOne.setNumber(number+1);
            service.updateById(serviceOne);

        }else{
            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            service.save(cart);
            serviceOne=cart;
        }

//         如果不存在 就加到购物车


        return Result.success(serviceOne);
    }


    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart cart){
        log.info("cart:{}",cart);

//        设置用户ID 指定当前是哪个用户的数据

        Long current_ID = BaseContext.getCurrentId();
        cart.setUserId(current_ID);

//        如果同一个菜品点了两份  数量就加一
        Long dishId= cart.getDishId();
//          构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper= new LambdaQueryWrapper<>();
        if(dishId!=null){
//添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }


//        查询一下添加的菜品或者套餐是否有没有在购物车


        ShoppingCart serviceOne = service.getOne(queryWrapper);

//        有user iD 有dish ID 或者  setmeal Id 那么

        int new_Number = serviceOne.getNumber()-1;
        if (new_Number>=0)
        serviceOne.setNumber(new_Number);




        service.updateById(serviceOne);

        return Result.success(serviceOne);
    }




    @DeleteMapping("/clean")
    public Result<String> clean(){


//        设置用户ID 指定当前是哪个用户的数据

        Long current_ID = BaseContext.getCurrentId();

//          构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper= new LambdaQueryWrapper<>();


        queryWrapper.eq(ShoppingCart::getUserId,current_ID);
        service.remove(queryWrapper);

        return Result.success("clean 成功");
    }


}
