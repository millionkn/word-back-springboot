package com.example.demo.entitis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class Lesson {
    String id;
    JSONArray data;
    JSONObject info;
}