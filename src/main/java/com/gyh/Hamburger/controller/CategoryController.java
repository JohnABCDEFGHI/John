package com.gyh.Hamburger.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.Category;
import com.gyh.Hamburger.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序添加
        queryWrapper.orderByDesc(Category::getUpdateTime);
        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    @PutMapping
    public Result<String> update( @RequestBody Category category){
        categoryService.updateById(category);
        return  Result.success("修改信息成功") ;

    }

    @DeleteMapping
    public Result<String> delete(Long ids){
        log.info("id:{}",ids);
        categoryService.remove(ids);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
//    使用实体接收  可用性更强
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询数据
        List<Category> list = categoryService.list(queryWrapper);
        //返回数据
        return Result.success(list);

    }



}
