package com.example.demo.controller;

import java.util.List;

import com.example.demo.entitis.Support;
import com.example.demo.mapper.WorkMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportController {
  @Autowired
  WorkMapper workMapper;

  @GetMapping("/support/lesson/{id}")
  List<Support> getSupportByLessonId(@PathVariable String id) {
    return workMapper.getSupportByLessonId(id);
  }

  @GetMapping("/support/word/{id}")
  List<Support> getSupportByWordId(@PathVariable String id) {
    return workMapper.getSupportByWordId(id);
  }

  @GetMapping("/support/component/{id}")
  List<Support> getSupportByComponentId(@PathVariable String id) {
    return workMapper.getSupportByComponentId(id);
  }
}