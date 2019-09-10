package com.example.demo.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/loginTest")
public class LoginTestController{
    
    @RequiresUser
    @GetMapping("/users")
    public void users(){}

    @RequiresRoles("测试分组")
    @GetMapping("/roles")
    public void roles() {}

    @RequiresRoles("不存在的分组")
    @GetMapping("/noroles")
    public void roles2() {}
    
}