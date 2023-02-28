package com.gyh.Hamburger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gyh.Hamburger.domain.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
