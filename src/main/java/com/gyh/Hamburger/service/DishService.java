package com.gyh.Hamburger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyh.Hamburger.domain.Dish;
import com.gyh.Hamburger.dto.DishDto;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    void updateWithFlavor(DishDto dishDto);
    DishDto getIDWithFlavor(Long id);


}
