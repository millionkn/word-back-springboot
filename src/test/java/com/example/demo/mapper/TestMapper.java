package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entitis.TestData;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface TestMapper{
    @Select("select * from test_table")
    @Results({
            @Result(property = "data", column = "test")
    })
    List<TestData> getAll();

    @Select("select * from test_table where id=#{id}")
    @Results({
        @Result(property = "data", column = "test")
    })
    TestData getById(int id);
}