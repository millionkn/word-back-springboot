package com.example.demo.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.mapper.WorkMapper;
import com.google.common.io.ByteStreams;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.jodah.expiringmap.ExpiringMap;

@RestController
public class UploadController {
  @Autowired
  WorkMapper workMapper;

  @Autowired
  ExpiringMap<String, byte[]> expiringMap;

  @RequiresUser
  @PostMapping("/upload")
  public JSONObject uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    JSONObject json = new JSONObject();
    if (file.isEmpty()) {
      json.put("err", "文件为空");
    } else {
      String hex = Long.toHexString(new Random().nextLong());
      expiringMap.put(hex, file.getBytes());
      json.put("code", hex);
    }
    return json;
  }

  @GetMapping(value = "/file/component/{id}")
  public String getComponentFile(@PathVariable("id") String id) throws IOException, SQLException {
    Blob blob = workMapper.readComponentFileById(id);
    return new String(ByteStreams.toByteArray(blob.getBinaryStream()));
  }
}