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
import com.formacaospring.dscommerce.tests.UserFactory;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String userSeller, userStock, userSellerPassword, userStockPassword, adminUsername, adminPassword;
	private String userSellerToken, userStockToken, adminToken, invalidToken;
	
	@BeforeEach
	private void setU() throws Exception{
		userSeller = "alex@gmail.com";
		userSellerPassword = "123456";
		userStock = "maria@gmail.com";
		userStockPassword = "123456";
		adminUsername = "admin@gmail.com";
		adminPassword = "123456";
		
		userSellerToken = tokenUtil.obtainAccessToken(mockMvc, userSeller, userSellerPassword);
		userStockToken = tokenUtil.obtainAccessToken(mockMvc, userStock, userStockPassword);
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
	}
	
	@Test
	public void findAllShouldReturnListOfCategoryDTOWhenAdminLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/categories")
					.header("Authorization", "Bearer " + adminToken)	
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.[0].id").value(1L));
		result.andExpect(jsonPath("$.[0].name").value("Livros"));
		result.andExpect(jsonPath("$.[1].id").value(2L));
		result.andExpect(jsonPath("$.[1].name").value("Eletrônicos"));
		result.andExpect(jsonPath("$.[2].id").value(3L));
		result.andExpect(jsonPath("$.[2].name").value("Computadores"));
	}
	
	@Test
	public void findAllShouldReturnForbiddenWhenSellerLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/categories")
					.header("Authorization", "Bearer " + userSellerToken)	
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void findAllShouldReturnListOfCategoryDTOWhenUserStockLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/categories")
					.header("Authorization", "Bearer " + userStockToken)	
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.[0].id").value(1L));
		result.andExpect(jsonPath("$.[0].name").value("Livros"));
		result.andExpect(jsonPath("$.[1].id").value(2L));
		result.andExpect(jsonPath("$.[1].name").value("Eletrônicos"));
		result.andExpect(jsonPath("$.[2].id").value(3L));
		result.andExpect(jsonPath("$.[2].name").value("Computadores"));
	}
	
	@Test
	public void findAllShouldReturnUnauthorizedWhenTokenInvalid() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/categories")
					.header("Authorization", "Bearer " + invalidToken)	
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
