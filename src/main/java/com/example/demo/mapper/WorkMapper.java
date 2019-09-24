package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entitis.Component;
import com.example.demo.entitis.Lesson;
import com.example.demo.entitis.Word;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from word where id=#{id}")
    Word getWordById(String id);

    @Select("select * from component where id=#{id}")
    Component getComponentById(String id);
}