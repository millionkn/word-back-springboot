package com.example.demo.entitis;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class Lesson {
  String id;
  JSONObject info;
  String owner;
  boolean showing;
}