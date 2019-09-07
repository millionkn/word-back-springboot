package com.example.demo;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {
	@Autowired
    private MockMvc mvc;
	@Test
	public void contextLoads() throws Exception {
		ResultActions result1 = mvc.perform(MockMvcRequestBuilders.get("/"));
		ResultActions result2 = mvc.perform(MockMvcRequestBuilders.get("/aaa"));
		ResultActions result3 = mvc.perform(MockMvcRequestBuilders.get("/bbb"));
		result1.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(result2.andReturn().getResponse().getContentAsString()))
		.andExpect(MockMvcResultMatchers.content().string(result3.andReturn().getResponse().getContentAsString()));
	}

}
