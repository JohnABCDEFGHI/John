package com.gyh.Hamburger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gyh.Hamburger.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
