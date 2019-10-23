package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

import com.example.demo.entitis.TestData;
import com.example.demo.mapper.TestMapper;
import com.example.demo.shiro.ShiroMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {
  @Autowired
  private MockMvc mvc;
  @Autowired
  private TestMapper userMapper;
  @Autowired
  ShiroMapper shiroMapper;

  @Test
  public void contextLoads() throws Exception {
    ResultActions result1 = mvc.perform(MockMvcRequestBuilders.get("/"));
    ResultActions result2 = mvc.perform(MockMvcRequestBuilders.get("/aaa"));
    ResultActions result3 = mvc.perform(MockMvcRequestBuilders.get("/bbb"));
    result1.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(result2.andReturn().getResponse().getContentAsString()))
        .andExpect(MockMvcResultMatchers.content().string(result3.andReturn().getResponse().getContentAsString()));
  }

  @Test
  public void testJSON() throws Exception {
    ResultActions result = mvc.perform(MockMvcRequestBuilders.get("/testJSON"));
    String string = result.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsString();
    JSONObject json = JSON.parseObject(string);
    TestCase.assertEquals(json.getString("key"), "value");
  }

  @Test
  public void mybatisTest() throws Exception {
    List<TestData> list = userMapper.getAll();
    TestCase.assertEquals(list.get(0).getData(), userMapper.getById(1).getData());
    TestCase.assertNotNull(list.get(0).getData());
    TestCase.assertEquals(list.get(1).getData(), userMapper.getById(2).getData());
    TestCase.assertNotNull(list.get(1).getData());
  }
}
