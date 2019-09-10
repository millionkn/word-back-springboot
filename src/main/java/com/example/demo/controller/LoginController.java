package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController{
    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject requestJSON){
        JSONObject json = new JSONObject();
        if (requestJSON.getString("username").isEmpty()||requestJSON.getString("password").isEmpty()){
            json.put("err", "用户名或密码为空");
            return json;
        }
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login( new UsernamePasswordToken(requestJSON.getString("username"), requestJSON.getString("password")) );
            if (currentUser.getPrincipal()==null) throw new AuthenticationException();
            json.put("userId",((Integer)currentUser.getPrincipal()).toString());
        } catch ( UnknownAccountException uae ) {
            json.put("err", "未知用户名");
        } catch ( IncorrectCredentialsException ice ) {
            json.put("err", "密码错误");
        } catch ( LockedAccountException lae ) {
            json.put("err", "用户被锁定");
        } catch ( AuthenticationException ae ) {
            json.put("err", ae.getMessage());
        }
        
        return json;
    }
    @PostMapping("/logout")
    public void logout(){
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }
}