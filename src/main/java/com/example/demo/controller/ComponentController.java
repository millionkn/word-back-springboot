package com.example.demo.controller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.JSONErrorMessage;
import com.example.demo.entitis.Component;
import com.example.demo.mapper.WorkMapper;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentController {
  @Autowired
  WorkMapper workMapper;

  @RequiresUser
  @PostMapping("/component")
  public void createComponent(@RequestBody JSONObject body) {
    JSONObject info = body.getJSONObject("info");
    List<String> word = body.getJSONArray("word").toJavaList(String.class);
    String fileCode = body.getString("fileCode");
    Component component = new Component();
    component.setInfo(info);
    component.setUploader((String) SecurityUtils.getSubject().getPrincipal());
    workMapper.createComponent(component);
    String componentId = component.getId();
    workMapper.setComponentSupportWord(componentId, word);
    workMapper.setComponentFileCode(componentId, fileCode);
  }

  @GetMapping(value = "/component/{id}")
  public Component getComponentById(@PathVariable("id") String id) {
    if ("default".equals(id)) {
      id = "1";
    }
    return workMapper.getComponentById(id);
  }

  @GetMapping("/component/currentUser/")
  @RequiresUser
  public List<Component> currentUserComponentList() {
    return workMapper.getComponentByUploader((String) SecurityUtils.getSubject().getPrincipal());
  }

  @RequiresUser
  @DeleteMapping(value = "/component/{id}")
  public void deleteComponent(@PathVariable("id") String id) {
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    if (workMapper.getComponentById(id).getUploader().equals(userId)) {
      workMapper.deleteComponent(id);
    }
  }

  @RequiresUser
  @PutMapping(value = "/component/{id}")
  public void updateComponentInfo(@PathVariable("id") String id, @RequestBody JSONObject body) throws JSONErrorMessage {
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    if (!workMapper.getComponentById(id).getUploader().equals(userId)) {
      throw new JSONErrorMessage("权限不足");
    }
    JSONObject info = body.getJSONObject("info");
    if (null != info) {
      workMapper.setComponentInfo(id, info);
    }
    JSONArray wordIdList = body.getJSONArray("word");
    if (wordIdList != null) {
      workMapper.setComponentSupportWord(id, wordIdList.toJavaList(String.class));
    }
    String fileCode = body.getString("fileCode");
    if (fileCode != null) {
      workMapper.setComponentFileCode(id, fileCode);
    }
  }

  @GetMapping("/component/word/{id}")
  public List<Component> getComponentByWord(@PathVariable String id) {
    return workMapper.getComponentByWord(id);
  }

  @GetMapping(value = "/component/search/{name}")
  public List<Component> searchComponent(@PathVariable String name) {
    return workMapper.searchComponentByName(name);
  }
}