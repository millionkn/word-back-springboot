package com.example.demo.mapper;

import java.sql.Blob;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entitis.Component;
import com.example.demo.entitis.Lesson;
import com.example.demo.entitis.Word;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WorkMapper {
    @Select("select id,info from lesson")
    List<Lesson> getLessonInfoList();

    @Select("select * from lesson where id=#{id}")
    Lesson getLessonById(String id);

    @Select("select * from lesson where `id` in (select `lessonId` from lesson_subscriber where userId=#{userId})")
    List<Lesson> getUserSubscriberedLesson(String userId);

    @Insert("insert `lesson_subscriber`(`userId`,`lessonId`) values(#{userId},#{lessonId});")
    void subscriberLesson(String userId, String lessonId);

    @Delete("delete from `lesson_subscriber` where `userId`=#{userId} and lessonId=#{lessonId}")
    void deleteSubscriberLesson(String userId, String lessonId);

    @Select("<script> " + "select * from word where id in "
            + "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> " + "   #{item} "
            + "</foreach>" + "</script> ")
    List<Word> searchWordById(List<String> list);

    @Select("select * from component where id=#{id}")
    Component getComponentById(String id);

    @Insert("insert `component`(`uploader`,`info`,`words`,`file`) values(#{uploader},#{info},#{words},#{blob})")
    void insertComponent(String uploader, JSONObject info, JSONArray words, Blob blob);

    @Select("select * from component where uploader=#{uploader}")
    List<Component> getComponentByUploader(String uploader);

    @Select("select * from word where `describe` like concat(concat('%',#{describe}),'%')")
    List<Word> searchWordByDescribe(String describe);

    @Select("select file from component where id=#{id}")
    Blob readComponentFileById(String id);

    @Delete("delete from `component` where `id`=#{id};")
    void deleteComponent(String id);

    @Update("update `component` set `file`=#{blob} where `id`=#{id}")
    void updataComponentFile(String id, Blob blob);

    @Update("update `component` set `info`=#{json} where `id`=#{id}")
    void updataComponentInfo(String id, JSONObject json);

    @Update("update `component` set `words`=#{jsonArray} where `id`=#{id}")
    void updataComponentWords(String id, JSONArray jsonArray);
}