package com.example.demo.mapper;

import java.sql.Blob;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entitis.Component;
import com.example.demo.entitis.Lesson;
import com.example.demo.entitis.Support;
import com.example.demo.entitis.Word;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WorkMapper {
  @Select("select * from lesson where showing=true")
  List<Lesson> getPublicedLesson();

  @Select("select * from lesson where id=#{id}")
  Lesson getLessonById(String id);

  @Insert("insert into `lesson` values (null,#{info},#{owner},#{showing})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void createLesson(Lesson lesson);

  @Delete("delete from `lesson` where id=#{lessonId};" + "delete from `lesson_data` where `lesson_id`=#{lessonId};")
  void deleteLesson(String lessonId);

  @Update("<script>" + "delete from `lesson_data` where lesson_id=#{lessonId};" + "<if test=\"supportIdList.size()>0\">"
      + "insert into `lesson_data` values <foreach item='item' index='index' collection='supportIdList' separator=','>(null,#{lessonId},#{item})</foreach>"
      + "</if>" + "</script>")
  void setLessonData(String lessonId, List<String> supportIdList);

  @Select("select * from component where id=#{id}")
  Component getComponentById(String id);

  @Select("SELECT `word`.* from `word` LEFT JOIN `support` on `support`.`word_id`= `word`.`id` where `support`.component_id=#{componentId}")
  List<Word> getComponentSupportWord(String componentId);

  @Insert("insert into `component` values (null,#{uploader},#{info},'')")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void createComponent(Component component);

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
  void setComponentInfo(String id, JSONObject json);

  @Update("<script> " + "delete from `support` where `component_id`=#{componentId};"
      + "<if test=\"wordIdList.size()>0\">" + "insert into `support` values"
      + "<foreach item='item' index='index' collection='wordIdList' separator=','> " + "(null,#{componentId},#{item})"
      + "</foreach>" + "</if>" + "</script> ")
  void setComponentSupportWord(String componentId, List<String> wordIdList);

  @Select("select * from `lesson` where owner=#{userId}")
  List<Lesson> getUserLessons(String userId);

  @Select("SELECT `support`.* from `support` LEFT JOIN `lesson_data` on `support`.`id`= `lesson_data`.`support_id`"
      + "where `lesson_data`.`lesson_id`=#{lessonId}")
  @Results({ @Result(column = "component_id", property = "componentId"),
      @Result(column = "word_id", property = "wordId") })
  List<Support> getSupportByLessonId(String lessonId);

  @Select("<script> select * from `word` where id in "
      + "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"
      + "</script> ")
  List<Word> wordCollection(List<String> wordIdList);
}