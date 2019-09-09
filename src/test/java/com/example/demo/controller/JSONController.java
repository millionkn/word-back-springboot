package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONController{
    @GetMapping("/testJSON")
    public JSONObject testJson(){
        JSONObject json = new JSONObject();
        json.put("key", "value");
        return json;
    }
}