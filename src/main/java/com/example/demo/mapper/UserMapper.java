package com.example.demo.mapper;

import com.example.demo.entitis.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper{
    @Select("select * from user where id=#{id}")
    User getById(Integer id);
}