package com.example.demo.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entitis.Component;
import com.example.demo.entitis.Lesson;
import com.example.demo.entitis.Word;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.WorkMapper;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Work {
    @Autowired
    WorkMapper workMapper;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/lessonInfoList")
    public List<Lesson> getLessonList() {
        return workMapper.getLessonInfoList();
    }

    @GetMapping("/lesson/{id}")
    public Lesson getLessonById(@PathVariable String id) {
        return workMapper.getLessonById(id);
    }

    @RequiresUser
    @GetMapping(value = "/currentUser")
    public JSONObject currentUser() {
        String id = (String) SecurityUtils.getSubject().getPrincipal();
        return (JSONObject) JSON.toJSON(userMapper.getById(id));
    }

    @RequiresUser
    @GetMapping(value = "/currentUser/lessonList")
    public List<Lesson> currentUserLessonList() {
        String id = (String) SecurityUtils.getSubject().getPrincipal();
        return workMapper.getUserSubscriberedLesson(id);
    }

    @RequiresUser
    @PostMapping(value = "/currentUser/lessonList")
    public void addCurrentUserLesson(@RequestBody JSONObject json) {
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        workMapper.subscriberLesson(userId, json.getString("lessonId"));
    }

    @RequiresUser
    @DeleteMapping(value = "/currentUser/lessonList")
    public void deleteCurrentUserLesson(@RequestBody JSONObject json) {
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        workMapper.deleteSubscriberLesson(userId, json.getString("lessonId"));
    }

    @GetMapping(value = "/word/{id}")
    public Word getWordById(@PathVariable("id") String id) {
        return workMapper.getWordById(id);
    }

    @GetMapping(value = "/component/{id}")
    public JSONObject getComponentById(@PathVariable("id") String id) {
        if ("default".equals(id)) {
            id = "1";
        }
        Component c = workMapper.getComponentById(id);
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(c));
        json.put("url", "/components/" + json.getString("fileName"));
        json.remove("fileName");
        return json;
    }
}