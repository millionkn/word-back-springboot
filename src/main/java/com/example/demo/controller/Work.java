package com.example.demo.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entitis.Component;
import com.example.demo.entitis.Lesson;
import com.example.demo.entitis.Word;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.WorkMapper;
import com.google.common.io.ByteStreams;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

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

    @PostMapping(value = "/get/wordCollection")
    public List<Word> getWordById(@RequestBody JSONArray idArray) {
        if (idArray.isEmpty()) {
            return List.of();
        }
        return workMapper.searchWordById(idArray.toJavaList(String.class));
    }

    @GetMapping(value = "/word")
    public List<Word> searchWord(@RequestParam("search") String describe) {
        return workMapper.searchWordByDescribe(describe);
    }

    @GetMapping(value = "/component/data/{id}")
    public String getComponentData(@PathVariable("id") String id) throws IOException, SQLException {
        Blob blob = workMapper.readComponentFileById(id);
        return new String(ByteStreams.toByteArray(blob.getBinaryStream()));
    }

    @GetMapping(value = "/component/{id}")
    public JSONObject getComponentById(@PathVariable("id") String id) {
        if ("default".equals(id)) {
            id = "1";
        }
        Component c = workMapper.getComponentById(id);
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(c));
        json.put("url", "/component/data/" + id);
        json.remove("fileName");
        return json;
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
    public JSONObject updateComponentFile(@PathVariable("id") String id,
            @RequestParam(value = "code", required = false) String code,
            @RequestBody(required = false) JSONObject json) {

        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        JSONObject ret = new JSONObject();
        if (!workMapper.getComponentById(id).getUploader().equals(userId)) {
            ret.put("err", "权限不足");
        } else {
            if (code != null) {
                byte[] temp = expiringMap.get(code);
                if (null == temp) {
                    ret.put("err", "code值无效或已过期");
                } else {
                    workMapper.updataComponentFile(id, new com.mysql.cj.jdbc.Blob(temp, null));
                }
            }
            if (json != null) {
                JSONObject info = json.getJSONObject("info");
                if (info != null) {
                    workMapper.updataComponentInfo(id, info);
                }
                JSONArray words = json.getJSONArray("words");
                if (words != null) {
                    workMapper.updataComponentWords(id, words);
                }
            }
        }
        return ret;
    }

    @Autowired
    ExpiringMap<String, byte[]> expiringMap;

    @Bean
    public ExpiringMap<String, byte[]> expiringMapBean() {
        return ExpiringMap.builder().expiration(10, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.CREATED)
                .build();
    }

    @RequiresUser
    @PostMapping("/upload")
    public JSONObject uploadFile(@RequestParam("file") MultipartFile file) {
        JSONObject json = new JSONObject();
        if (file.isEmpty()) {
            json.put("err", "文件为空");
        } else {
            String hex = Long.toHexString(new Random().nextLong());
            try {
                expiringMap.put(hex, file.getBytes());
                json.put("code", hex);
            } catch (IOException e) {
                json.put("err", e.getMessage());
            }
        }
        return json;
    }

    @RequiresUser
    @PostMapping("/component")
    public JSONObject uploadComponent(@RequestBody JSONObject json, @RequestParam("code") String fileCode)
            throws IOException {
        JSONObject info = json.getJSONObject("info");
        JSONArray words = json.getJSONArray("words");
        JSONObject ret = new JSONObject();
        byte[] temp = expiringMap.get(fileCode);
        if (null == temp) {
            ret.put("err", "code值无效或已过期");
        } else {
            workMapper.insertComponent((String) SecurityUtils.getSubject().getPrincipal(), info, words,
                    new com.mysql.cj.jdbc.Blob(temp, null));
        }
        return ret;
    }

    @GetMapping("/currentUser/component")
    @RequiresUser
    public List<Component> currentUserComponentList() {
        return workMapper.getComponentByUploader((String) SecurityUtils.getSubject().getPrincipal());
    }
}