package com.gyh.Hamburger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.gyh.Hamburger.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
