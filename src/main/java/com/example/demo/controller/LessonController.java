package com.example.demo.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entitis.Lesson;
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
public class LessonController {
  @Autowired
  WorkMapper workMapper;

  @GetMapping("/lesson")
  public List<Lesson> getLessonList() {
    return workMapper.getPublicedLesson();
  }

  @GetMapping("/lesson/{id}")
  public Lesson getLessonById(@PathVariable String id) {
    return workMapper.getLessonById(id);
  }

  @RequiresUser
  @GetMapping(value = "/lesson/currentUser")
  public List<Lesson> currentUserLessonList() {
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    return workMapper.getUserLessons(userId);
  }

  @RequiresUser
  @PostMapping(value = "/lesson")
  public void createLesson(@RequestBody JSONObject body) {
    JSONObject lessonJSON = body.getJSONObject("lesson");
    JSONObject info = lessonJSON.getJSONObject("info");
    lessonJSON.put("info", null);
    Lesson lesson = lessonJSON.toJavaObject(Lesson.class);
    lesson.setInfo(info);
    List<String> supportIdList = body.getJSONArray("support").toJavaList(String.class);
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    lesson.setOwner(userId);
    workMapper.createLesson(lesson);
    workMapper.setLessonData(lesson.getId(), supportIdList);
  }

  @RequiresUser
  @DeleteMapping(value = "/lesson/{id}")
  public void deleteCurrentUserLesson(@PathVariable String id) {
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    if (workMapper.getLessonById(id).getOwner().equals(userId)) {
      workMapper.deleteLesson(id);
    }
  }

  @PutMapping(value = "/lesson/{id}")
  public void updateLesson(@PathVariable String id, @RequestBody JSONObject body) {
    String userId = (String) SecurityUtils.getSubject().getPrincipal();
    if (workMapper.getLessonById(id).getOwner().equals(userId)) {

      workMapper.setLessonInfo(id, body.getJSONObject("info"));
      workMapper.setLessonData(id, body.getJSONArray("support").toJavaList(String.class));
      workMapper.setLessonPubliced(id, body.getBoolean("showing"));
    }
  }
}