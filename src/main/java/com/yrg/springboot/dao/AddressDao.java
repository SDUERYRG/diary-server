package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.yrg.springboot.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AddressDao extends BaseMapper<Address> {
}
