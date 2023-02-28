package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.domain.Dish;
import com.gyh.Hamburger.domain.DishFlavor;
import com.gyh.Hamburger.dto.DishDto;
import com.gyh.Hamburger.mapper.DishMapper;
import com.gyh.Hamburger.service.DishFlavorService;
import com.gyh.Hamburger.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService service ;

    /**
     * 新增菜品 并且保存对应口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //只是保存菜品的基本信息
        this.save(dishDto);
        Long dishId =dishDto.getId();
        //保存菜品口味数据到菜品口味表
        List<DishFlavor> flavors= dishDto.getFlavors();
        flavors =flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        service.saveBatch(flavors);
    }


    @Override
    public DishDto getIDWithFlavor(Long id) {

        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors =service.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }


    /**
     * 修改菜品 并且保存对应口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
//        更新dish基本表信息
        this.updateById(dishDto);
//        清理当前口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        service.remove(queryWrapper);
//        插入当前口味信息
        List<DishFlavor> flavors= dishDto.getFlavors();
        flavors =flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        service.saveBatch(flavors);



    }





}
