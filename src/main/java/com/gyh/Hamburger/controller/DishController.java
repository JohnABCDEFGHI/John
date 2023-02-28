package com.gyh.Hamburger.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gyh.Hamburger.common.Result;
import com.gyh.Hamburger.domain.Category;
import com.gyh.Hamburger.domain.Dish;
import com.gyh.Hamburger.domain.DishFlavor;
import com.gyh.Hamburger.dto.DishDto;
import com.gyh.Hamburger.service.CategoryService;
import com.gyh.Hamburger.service.DishFlavorService;
import com.gyh.Hamburger.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
@Api("菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 这里写错了 不能只接受Dish 类
     * @param dish
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品接口")
    public Result<String> save(@RequestBody DishDto dish){
        log.info("dish:{}",dish);
//        dishService.save(dish);  要操作两张表

        dishService.saveWithFlavor(dish);
        return Result.success("新增菜品成功");
    }


    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @ApiOperation("菜品信息分页查询接口")
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);


        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        名字不能空
        queryWrapper.like(name!=null,Dish::getName,name);

        //添加排序添加
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行查询
        dishService.page(pageInfo,queryWrapper);

//        对象拷贝 因为Dish里面没有菜品名称这个属性 但是DishDto中有这个属性  我们可以将Dish对象属性拷贝到 DishDto中
//          第三个参数是忽略掉的属性
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records" );

        List<Dish> records = pageInfo.getRecords();
//        得到新的dto集合
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto =new DishDto();
//            普通属性拷贝给Dto
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
//            这里报了一个error  通过ID可能没查到数据 报了空指针异常
            String c = category.getName();
            dishDto.setCategoryName(c);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return Result.success(dishDtoPage);
    }
    
    @PutMapping
    @ApiOperation("更新菜品接口")
    public Result<String> update( @RequestBody DishDto dishDto){

        Set keys = redisTemplate.keys("dish_*");
        log.info("keys:{}",keys);
        redisTemplate.delete(keys);

        dishService.updateWithFlavor(dishDto);
        return  Result.success("修改信息成功") ;

    }

    @DeleteMapping
    @ApiOperation("删除菜品接口")
    public Result<String> delete(@RequestParam List<Long> ids){
        log.info("id:{}",ids);
        dishService.removeByIds(ids);
        return Result.success("删除成功");
    }

    /**
     *通过ID查询菜品信息和口味信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品接口")
    public Result<DishDto> page(@PathVariable("id") long id){
        log.info("id = {}",id);
        return Result.success(dishService.getIDWithFlavor(id));
    }


    @GetMapping("/list")
    @ApiOperation("菜品信息列表接口")
    public Result<List<DishDto>> list (Dish dish){
        List<DishDto> list = null;

//        先从redis 获取缓存数据

//        分类的iD  状态 构造 key
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        list= (List<DishDto>) redisTemplate.opsForValue().get(key);

        if (list==null){
//        如果不存在
            LambdaQueryWrapper<Dish> queryWrapper= new LambdaQueryWrapper<>();
            queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
            //添加排序条件
            //查询状态为1的
            queryWrapper.eq(Dish::getStatus,1);
            queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
            List<Dish> dishes = dishService.list(queryWrapper);

            list =dishes.stream().map((item)->{
                DishDto dishDto =new DishDto();
//            普通属性拷贝给Dto
                BeanUtils.copyProperties(item,dishDto);
                Long categoryId = item.getCategoryId();
                Category category = categoryService.getById(categoryId);
//            这里报了一个error  通过ID可能没查到数据 报了空指针异常
                String c = category.getName();
                dishDto.setCategoryName(c);
                Long id = item.getId();
                LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(DishFlavor::getDishId,id);
                List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);

                dishDto.setFlavors(dishFlavorList);

                return dishDto;
            }).collect(Collectors.toList());
//            放Redis 60分钟过期
            redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
        }

        return Result.success(list);
    }








}
