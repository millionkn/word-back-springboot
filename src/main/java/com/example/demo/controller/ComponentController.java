package com.example.demo.controller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.jodah.expiringmap.ExpiringMap;

@RestController
public class ComponentController {
  @Autowired
  WorkMapper workMapper;
  @Autowired
  ExpiringMap<String, byte[]> expiringMap;

  @RequiresUser
  @PostMapping("/component")
  public JSONObject createComponent(@RequestBody JSONObject json, @RequestParam("code") String fileCode) {
    JSONObject ret = new JSONObject();
    byte[] temp = expiringMap.get(fileCode);
    if (null == temp) {
      ret.put("err", "code值无效或已过期");
    } else {
      Component component = new Component();
      component.setInfo(json.getJSONObject("info"));
      component.setUploader((String) SecurityUtils.getSubject().getPrincipal());
      workMapper.createComponent(component);
      String componentId = component.getId();
      workMapper.updataComponentFile(componentId, new com.mysql.cj.jdbc.Blob(temp, null));
      workMapper.setComponentSupportWord(componentId, json.getJSONArray("word").toJavaList(String.class));
    }
    return ret;
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
  @PutMapping(value = "/component/{componentId}")
  public JSONObject updateComponent(@PathVariable("componentId") String componentId,
      @RequestParam(value = "code", required = false) String code, @RequestBody(required = false) JSONObject json) {

    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    JSONObject ret = new JSONObject();
    if (!workMapper.getComponentById(componentId).getUploader().equals(userId)) {
      ret.put("err", "权限不足");
    } else {
      if (code != null) {
        byte[] temp = expiringMap.get(code);
        if (null == temp) {
          ret.put("err", "code值无效或已过期");
        } else {
          workMapper.updataComponentFile(componentId, new com.mysql.cj.jdbc.Blob(temp, null));
        }
      }
      if (json != null) {
        JSONObject info = json.getJSONObject("info");
        if (info != null) {
          workMapper.setComponentInfo(componentId, info);
        }
        JSONArray words = json.getJSONArray("word");
        if (words != null) {
          workMapper.setComponentSupportWord(componentId, words.toJavaList(String.class));
        }
      }
    }
    return ret;
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