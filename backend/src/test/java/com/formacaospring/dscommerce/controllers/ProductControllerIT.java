package com.formacaospring.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacaospring.dscommerce.dto.ProductDTO;
import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.tests.TokenUtil;

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

	private Long existingProductId, nonExistingProductId, dependentProductId; 
	private String usernameAdmin, usernameClient, passwordAdmin, passwordClient, tokenAdmin, tokenClient, invalidToken,
			productName;
	private Product product;
	private ProductDTO productDTO;


	@BeforeEach
	void setUp() throws Exception {
		existingProductId = 2L;
		nonExistingProductId = 100L;
		dependentProductId = 3L;
		
		usernameAdmin = "alex@gmail.com";
		passwordAdmin = "123456";
		usernameClient = "maria@gmail.com";
		passwordClient = "123456";
		
		productName = "Macbook Pro";
		
		tokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, passwordAdmin);
		tokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, passwordClient);
		invalidToken = tokenAdmin + "xpto";
		
		Category category = new Category(2L, "Eletronics");
		product = new Product(null, "Playstation 5", "blablablablablablablablablabla", 3000.0, 2,
				"blablablablablablablablablabla");
		product.getCategories().add(category);
		productDTO = new ProductDTO(product);
	}

	@Test
	public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value(
				"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
	}

	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products?name={productName}", productName)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value(
				"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}

	@Test
	public void insertShouldInsertProductDTOCreatedWhenAdminLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L));
		result.andExpect(jsonPath("$.name").value("Playstation 5"));
		result.andExpect(jsonPath("$.description").value("blablablablablablablablablabla"));
		result.andExpect(jsonPath("$.price").value(3000.00));
		result.andExpect(jsonPath("$.imgUrl").value("blablablablablablablablablabla"));
		result.andExpect(jsonPath("$.categories[0].id").value(2L));
	}

	@Test
	public void insertShouldThrowUnaprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {

		product.setName("ab");
		productDTO = new ProductDTO(product);

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldThrowUnaprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {

		product.setDescription("ab");
		productDTO = new ProductDTO(product);

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldThrowUnaprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {

		product.setPrice(-50.0);
		productDTO = new ProductDTO(product);

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldThrowUnaprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {

		product.setPrice(0.0);
		productDTO = new ProductDTO(product);

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldThrowUnaprocessableEntityWhenAdminLoggedAndProductHasNoCategory() throws Exception {

		product.getCategories().clear();
		productDTO = new ProductDTO(product);

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products").header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + tokenClient)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWheninvalidToken() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + invalidToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExistsAndAdminLogged() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + tokenAdmin)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", nonExistingProductId)
						.header("Authorization", "Bearer " + tokenAdmin)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}
	
	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteShouldReturnBadRequestWhenIdIsDependentAndAdminLogged() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", dependentProductId)
						.header("Authorization", "Bearer " + tokenAdmin)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenIdExistsAndClientLogged() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + tokenClient)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception{
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", existingProductId)
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

}
