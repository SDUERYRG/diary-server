package com.yrg.springboot.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yrg.springboot.dao.AddressDao;
import com.yrg.springboot.entity.Address;
import com.yrg.springboot.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressDao, Address> implements AddressService {
}
