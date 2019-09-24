package com.example.demo;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.shiro.ShiroMapper;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	ShiroMapper shiroMapper;
	private MockHttpSession session;

	@Before
	public void onBefore() throws Exception {
		session = new MockHttpSession();
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/users").session(session))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());

		JSONObject requestJSON = new JSONObject();
		requestJSON.put("username", "测试账号");
		requestJSON.put("password", "password");
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON).content(requestJSON.toJSONString()).session(session);
		mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/users").session(session))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@After
	public void onAfter() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/users").session(session))
				.andExpect(MockMvcResultMatchers.status().isOk());
		mvc.perform(MockMvcRequestBuilders.post("/logout").session(session))
				.andExpect(MockMvcResultMatchers.status().isOk());
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/users").session(session))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	public void testRoles() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/roles").session(session))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testWithOutRoles() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/loginTest/noroles").session(session))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void sha256hashTest() {
		String password = new Sha256Hash("password", shiroMapper.getSalt("1")).toString();
		TestCase.assertEquals(shiroMapper.getPassword("1"), password);
	}
}
