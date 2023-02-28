package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.domain.Employee;
import com.gyh.Hamburger.mapper.EmployeeMapper;
import com.gyh.Hamburger.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
