package com.gyh.Hamburger.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.Category;
import com.gyh.Hamburger.domain.Setmeal;
import com.gyh.Hamburger.dto.SetmealDto;
import com.gyh.Hamburger.service.SetMealDishService;
import com.gyh.Hamburger.service.CategoryService;
import com.gyh.Hamburger.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private SetMealDishService service;

    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto dto){

        log.info("套餐数据：{}",dto);

        setMealService.saveWithDish(dto);

        return Result.success("添加套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> pageResult(int page,int pageSize,String name){

        //分页构造器

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page,pageSize);





        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name !=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageInfo,queryWrapper);
//        进行对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> pageInfoRecords = pageInfo.getRecords();

        List<SetmealDto> list = pageInfoRecords.stream().map((item)->{

            SetmealDto dto= new SetmealDto();
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if(byId!=null){
                dto.setCategoryName(byId.getName());
            }
            BeanUtils.copyProperties(item,dto);
            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        //页面上套餐分类 展示不出来 因为只有ID

        return Result.success(dtoPage );

    }

    /**
     * 删除套餐 用List接收可以进行批量删除
     * @param ids
     * @return
     */
    @DeleteMapping()
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> deleteSetseal(@RequestParam List<Long> ids){

        log.info("前端传过来的id:{}",ids);
        setMealService.deleteWithDish(ids);

        return Result.success("删除成功");
    }


//    只有停售才能删除套餐
    @PostMapping("/status/{status}")
    public  Result<String> stopSeal(@PathVariable("status") Integer status,@RequestParam("ids") List<Long> ids){
           log.info("param:{}",ids);


        List<Setmeal> list = ids.stream().map((item) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(item);
            return setmeal;
        }).collect(Collectors.toList());
        setMealService.updateBatchById(list);

        return Result.success("修改状态成功");
    }

//开启缓存 Result一定要实现缓存接口
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);

        return Result.success(list);
    }




}
