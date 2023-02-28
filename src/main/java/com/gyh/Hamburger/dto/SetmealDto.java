package com.gyh.Hamburger.dto;


import com.gyh.Hamburger.domain.Setmeal;
import com.gyh.Hamburger.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
