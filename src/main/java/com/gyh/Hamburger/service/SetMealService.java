package com.gyh.Hamburger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyh.Hamburger.domain.Setmeal;
import com.gyh.Hamburger.dto.SetmealDto;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto dto);
    void deleteWithDish(List<Long> ids);
    void stopSetseal(Integer status,List<Long> ids);
}
