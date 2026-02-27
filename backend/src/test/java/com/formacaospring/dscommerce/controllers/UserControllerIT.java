package com.formacaospring.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.formacaospring.dscommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String sellerUsername, sellerPassword, stockUsername, stockPassword, adminUsername, adminPassword;
	private String sellerToken, adminToken, stockToken, invalidToken;
	
	@BeforeEach
	void setUp() throws Exception {
		
		sellerUsername = "jaja24@gmail.com";
		sellerPassword = "123456";
		stockUsername = "maria@gmail.com";
		stockPassword = "123456";
		adminUsername = "admin@gmail.com";
		adminPassword = "123456";
				
		sellerToken = tokenUtil.obtainAccessToken(mockMvc, sellerUsername, sellerPassword);
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		stockToken = tokenUtil.obtainAccessToken(mockMvc, stockUsername, stockPassword);
		invalidToken = adminToken + "xpto"; // Simulates a wrong token
	}
	
	@Test
	public void getMeShouldReturnUserDTOWhenAdminLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/users/me")
					.header("Authorization", "Bearer " + adminToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.username").value("admin@gmail.com"));
		result.andExpect(jsonPath("$.email").value("admin@gmail.com"));		
		result.andExpect(jsonPath("$.roles").exists());
	}
	
	@Test
	public void getMeShouldReturnUserDTOWhenSellerLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/users/me")
					.header("Authorization", "Bearer " + sellerToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.username").value("jaja24@gmail.com"));
		result.andExpect(jsonPath("$.email").value("jaja24@gmail.com"));	
		result.andExpect(jsonPath("$.roles").exists());
	}
	
	@Test
	public void getMeShouldReturnUserDTOWhenUserStockLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/users/me")
					.header("Authorization", "Bearer " + stockToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.username").value("maria@gmail.com"));
		result.andExpect(jsonPath("$.email").value("maria@gmail.com"));		
		result.andExpect(jsonPath("$.roles").exists());
	}
	
	@Test
	public void getMeShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/users/me")
					.header("Authorization", "Bearer " + invalidToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
