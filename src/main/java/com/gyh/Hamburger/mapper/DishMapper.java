package com.gyh.Hamburger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyh.Hamburger.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
