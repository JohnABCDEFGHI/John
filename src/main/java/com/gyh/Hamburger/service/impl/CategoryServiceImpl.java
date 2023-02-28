package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.common.MyCustomException;
import com.gyh.Hamburger.domain.Category;
import com.gyh.Hamburger.domain.Dish;
import com.gyh.Hamburger.domain.Setmeal;
import com.gyh.Hamburger.mapper.CategoryMapper;
import com.gyh.Hamburger.service.CategoryService;
import com.gyh.Hamburger.service.DishService;
import com.gyh.Hamburger.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;


    /**
     *  根据ID删除分类 在删除之前进行判断  是否关联菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
//        查询当前分了是否关联菜品 如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Dish>dishLambdaQueryWrapper =new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count >0){
//            已经关联菜品了  抛出一个业务异常

                throw new MyCustomException("已纪关联菜品信息了");
        }

//        查询当前分类是否关联套餐，如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Setmeal>setmealLambdaQueryWrapper =new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int cnt = setMealService .count(setmealLambdaQueryWrapper);

        if(cnt >0){
//            已经关联菜品了  抛出一个业务异常
            throw new MyCustomException("已纪关联菜品信息了");
        }

//        正常删除

        super.removeById(id);
    }
}
