package com.formacaospring.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacaospring.dscommerce.dto.ProductDTO;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.tests.ProductFactory;
import com.formacaospring.dscommerce.tests.TokenUtil;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String usernameAdmin, usernameClient, passwordAdmin, passwordClient, tokenAdmin, tokenClient, invalidToken;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception{
		usernameAdmin = "alex@gmail.com";
		passwordAdmin = "123456";
		usernameClient = "maria@gmail.com";
		passwordClient = "123456";
		tokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, passwordAdmin);
		tokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, passwordClient);
		invalidToken = tokenAdmin + "xpto";	
	}
	
	@Test
	public void findAllShouldReturnProductMinDTOPagedByName() throws Exception{
		ResultActions result = mockMvc.perform(get("/products?name=mac")
				.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));			
	}
	
	@Test
	public void insertShouldInsertProductWhenAdminLoggedAndValidData() throws Exception {
		product = ProductFactory.createProduct();
		product.setId(null);
		ProductDTO productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/products")
						.header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());			
	}
	
	@Test
	public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndNameInvalid() throws Exception{
		product = ProductFactory.createProduct();
		product.setId(null);
		product.setName("  ");
		ProductDTO productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + tokenAdmin)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());		
	}
	
	

}
