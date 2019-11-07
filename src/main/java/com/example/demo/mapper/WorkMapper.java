package com.example.demo.mapper;

import java.io.InputStream;
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

  @Update("update `lesson` set `showing`=#{publiced} where `id`=#{lessonId} ")
  void setLessonPubliced(String lessonId, Boolean publiced);

  @Update("update  `lesson` set `info`=#{info} where `id`=#{lessonId}")
  void setLessonInfo(String lessonId, JSONObject info);

  @Select("select * from component where id=#{id}")
  Component getComponentById(String id);

  @Select("SELECT `word`.* from `word` LEFT JOIN `support` on `support`.`word_id`= `word`.`id` where `support`.component_id=#{componentId}")
  List<Word> getComponentSupportWord(String componentId);

  @Insert("insert into `component` values (null,#{uploader},#{info},0)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void createComponent(Component component);

  @Select("select * from component where uploader=#{uploader}")
  List<Component> getComponentByUploader(String uploader);

  @Select("select * from word where `describe` like concat(concat('%',#{describe}),'%')")
  List<Word> searchWordByDescribe(String describe);

  @Delete("delete from `component` where `id`=#{id};")
  void deleteComponent(String id);

  @Update("update `component` set `fileCode`=#{fileCode} where `id`=#{id}")
  void setComponentFileCode(String id, String fileCode);

  @Select("select `fileCode` from `component` where `id`=#{id}")
  String getComponentFileCode(String id);

  @Select("select `blob` from `blob_data` where id=#{id}")
  InputStream getBinary(String id);

  @Select("insert into `blob_data` values (null,#{inputStream});select LAST_INSERT_ID()")
  String setBinary(InputStream inputStream);

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

  @Select("SELECT * from `support` where `word_id`=#{wordId}")
  @Results({ @Result(column = "component_id", property = "componentId"),
      @Result(column = "word_id", property = "wordId") })
  List<Support> getSupportByWordId(String wordId);

  @Select("SELECT * from `support` where `component_id`=#{componentId}")
  @Results({ @Result(column = "component_id", property = "componentId"),
      @Result(column = "word_id", property = "wordId") })
  List<Support> getSupportByComponentId(String componentId);

  @Select("select `component`.* from `component` left join `support` on `component`.id = `support`.component_id"
      + "where `support`.word_id=#{wordId}")
  List<Component> getComponentByWord(String wordId);

  @Select("select * from `component` where info->'$.name' like concat(concat('%',#{name}),'%')")
  List<Component> searchComponentByName(String name);

  @Select("<script> select * from `word` where id in "
      + "(null<foreach item='item' index='index' collection='list'>,#{item}</foreach>)" + "</script> ")
  List<Word> wordCollection(List<String> wordIdList);
}