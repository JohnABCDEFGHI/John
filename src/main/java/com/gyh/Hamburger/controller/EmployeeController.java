package com.gyh.Hamburger.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.Employee;

import com.gyh.Hamburger.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService service;





    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

//1.将页面提交的密码password进行**md5加密**
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

//        2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryChainWrapper =new LambdaQueryWrapper<>();
        queryChainWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = service.getOne(queryChainWrapper);
//        3.如果没有查询到返回登录失败结果
        if (one==null){
            return Result.error("登录失败");
        }
//        4.密码对比，如果不一致返回登录失败结果
        if(!one.getPassword().equals(password)){
            return Result.error("登录失败");
        }

//        5.查看员工状态，如果为禁用状态，则返回禁用结果

        if(one.getStatus() ==0){
            return Result.error("已经禁用");
        }

//
//        6.登录成功，将员工Id存到Session返回登录结果

        log.info("登陆成功"+one.getId());
        request.getSession().setAttribute("employee",one.getId());
        return Result.success(one);
    }

@PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
//        清理session中的当前员工登陆ID
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工的信息：{}",employee.toString());
        //设置初始密码，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //强转为long类型
//        Long empID = (Long)request.getSession().getAttribute("employee");
////到后期这里需要进行优化 通过一个线程来获取当前用户的ID
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);

        service.save(employee);
        return Result.success("添加员工成功");
    }


    @GetMapping("/{id}")
    public Result<Employee> page(@PathVariable("id") long id){
        log.info("id = {}",id);
// 添加条件查询  byID 1515879467866742786
        return Result.success(service.getById(id));
    }



    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if(name !=null){
            //添加过滤条件 如果name不为空 那么就添加搜索条件
            queryWrapper.like(StringUtils.isEmpty(name),Employee::getName,name);
        }


        //添加排序添加
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        service.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }


    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee){
        //在这有精度损失 Js处理16位是精确的 long 是19位 所以会出错
        System.out.println("invoke");
        log.info(employee.toString());
        log.info("修改过后的Employee："+employee.toString());
        service.updateById(employee);
        return  Result.success("修改信息成功") ;

    }









}
