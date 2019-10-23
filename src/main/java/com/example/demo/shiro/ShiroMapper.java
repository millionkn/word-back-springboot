package com.example.demo.shiro;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShiroMapper {
  @Select("select id from user where username=#{username}")
  String getIdByUsername(String username);

  @Select("select password from user where id=#{id}")
  String getPassword(String id);

  @Select("select salt from user where id=#{id}")
  String getSalt(String id);

  @Select("select role.name from user_role left join role on user_role.role=role.id where user_role.user=#{id}")
  List<String> findRoles(String id);
}