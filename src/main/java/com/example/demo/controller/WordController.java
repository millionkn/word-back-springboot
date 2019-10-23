package com.example.demo.controller;

import java.util.List;

import com.example.demo.entitis.Word;
import com.example.demo.mapper.WorkMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordController {
  @Autowired
  WorkMapper workMapper;

  @GetMapping(value = "/word/search/{describe}")
  public List<Word> searchWord(@PathVariable String describe) {
    return workMapper.searchWordByDescribe(describe);
  }

  @GetMapping(value = "/word/component/{id}")
  public List<Word> getComponentSupportWord(@PathVariable("id") String componentId) {
    return workMapper.getComponentSupportWord(componentId);
  }

  @PostMapping(value = "/word")
  public List<Word> wordCollection(@RequestBody List<String> list) {
    return workMapper.wordCollection(list);
  }
}