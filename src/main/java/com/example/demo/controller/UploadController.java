package com.example.demo.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.JSONErrorMessage;
import com.example.demo.mapper.WorkMapper;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
  @Autowired
  WorkMapper workMapper;

  @RequiresUser
  @PostMapping("/upload")
  public JSONObject uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    JSONObject json = new JSONObject();
    json.put("fileCode", workMapper.setBinary(file.getInputStream()));
    return json;
  }

  @GetMapping(value = "/blob/{fileCode}")
  public void getBlob(@PathVariable String fileCode, HttpServletResponse res) throws JSONErrorMessage, IOException {
    InputStream is = workMapper.getBinary(fileCode);
    if (null == is) {
      throw new JSONErrorMessage("fileCode is not found");
    }
    IOUtils.copy(is, res.getOutputStream());
  }

  @GetMapping(value = "/fileCode/component/{id}")
  public JSONObject getComponentBlob(@PathVariable String id, HttpServletResponse res) throws JSONErrorMessage {
    String fileCode = workMapper.getComponentFileCode(id);
    if (null == fileCode) {
      throw new JSONErrorMessage("component is not found");
    }
    JSONObject json = new JSONObject();
    json.put("fileCode", fileCode);
    return json;
  }
}