package com.example.demo.entitis;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class Component {
  String id;
  String uploader;
  JSONObject info;
}