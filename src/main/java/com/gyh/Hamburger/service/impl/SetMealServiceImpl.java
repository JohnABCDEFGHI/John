package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.common.MyCustomException;
import com.gyh.Hamburger.domain.Setmeal;
import com.gyh.Hamburger.domain.SetmealDish;
import com.gyh.Hamburger.dto.SetmealDto;
import com.gyh.Hamburger.mapper.SetMealMapper;
import com.gyh.Hamburger.service.SetMealDishService;
import com.gyh.Hamburger.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;


    @Override
    @Transactional
    public void saveWithDish(SetmealDto dto) {
//        保存我们套餐的基本信息 操作setMeal 执行insert
//        保存套餐和菜品的关联信息

        this.save(dto);
        List<SetmealDish> setmealDishes =dto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
          item.setSetmealId(dto.getId());
          return item;
        }).collect(Collectors.toList());

        setMealDishService.saveBatch(setmealDishes);




    }

    @Override
    public void deleteWithDish(List<Long> ids) {

//        查询套餐状态是否能删除

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if(count>0){
            throw new MyCustomException("套餐正在售卖中，不能删除");
        }
//        如果不能删除 抛出一个异常

//        如果可以删除 先删除套餐数据
        this.removeByIds(ids);
//        再删除关系表
        LambdaQueryWrapper<SetmealDish> queryDish = new LambdaQueryWrapper<>();
        queryDish.in(SetmealDish::getSetmealId,ids);
        setMealDishService.remove(queryDish);





        this.removeByIds(ids);
    }

    @Override
    public void stopSetseal(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new
                LambdaQueryWrapper<>();

    }
}
