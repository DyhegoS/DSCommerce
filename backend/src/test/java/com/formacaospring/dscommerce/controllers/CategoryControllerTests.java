package com.formacaospring.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.formacaospring.dscommerce.dto.CategoryDTO;
import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.services.CategoryService;
import com.formacaospring.dscommerce.tests.CategoryFactory;
import com.formacaospring.dscommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CategoryService service;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String userSeller, userStock, userSellerPassword, userStockPassword, adminUsername, adminPassword;
	private String userSellerToken, userStockToken, adminToken, invalidToken;

	private List<CategoryDTO> listCategories;
	private Category category;
	private CategoryDTO categoryDTO;

	@BeforeEach
	void setUp() throws Exception{
		
		userSeller = "alex@gmail.com";
		userSellerPassword = "123456";
		userStock = "maria@gmail.com";
		userStockPassword = "123456";
		adminUsername = "admin@gmail.com";
		adminPassword = "123456";
		
		userSellerToken = tokenUtil.obtainAccessToken(mockMvc, userSeller, userSellerPassword);
		userStockToken = tokenUtil.obtainAccessToken(mockMvc, userStock, userStockPassword);
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		category = CategoryFactory.createCategory(2L, "Home products");
		categoryDTO = new CategoryDTO(category);

		listCategories = List.of(categoryDTO);

		Mockito.when(service.findAll()).thenReturn(listCategories);
	}

	@Test
	public void findAllShouldReturnListOfCategoryDTOWhenAdminLogged() throws Exception {

		ResultActions result = 
				mockMvc.perform(get("/categories")
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].id").exists());
		result.andExpect(jsonPath("$[0].id").value(2L));
		result.andExpect(jsonPath("$[0].name").exists());
		result.andExpect(jsonPath("$[0].name").value("Home products"));
	}
	
	@Test
	public void findAllShouldReturnListOfCategoryDTOWhenUserStockLogged() throws Exception {

		ResultActions result = 
				mockMvc.perform(get("/categories")
						.header("Authorization", "Bearer " + userStockToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].id").exists());
		result.andExpect(jsonPath("$[0].id").value(2L));
		result.andExpect(jsonPath("$[0].name").exists());
		result.andExpect(jsonPath("$[0].name").value("Home products"));
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
	public void findAllShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

		ResultActions result = 
				mockMvc.perform(get("/categories")
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}

}
