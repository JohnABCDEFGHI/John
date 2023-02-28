package com.gyh.Hamburger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gyh.Hamburger.domain.AddressBook;
import com.gyh.Hamburger.mapper.AddressBookMapper;
import com.gyh.Hamburger.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressService {
}
