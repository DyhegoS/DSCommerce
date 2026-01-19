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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacaospring.dscommerce.dto.ProductDTO;
import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.tests.OrderFactory;
import com.formacaospring.dscommerce.tests.ProductFactory;
import com.formacaospring.dscommerce.tests.TokenUtil;
import com.formacaospring.dscommerce.tests.UserFactory;

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

    private long existingProductId, nonExistingProductId, dependentProductId;
	private String usernameAdmin, usernameClient, passwordAdmin, passwordClient, tokenAdmin, tokenClient, invalidToken, productName;
	private Product product;
	private Order order;
	private User client;
	
	@BeforeEach
	void setUp() throws Exception{
        existingProductId = 1L;
        nonExistingProductId = 1000L;
        dependentProductId = 1L;
        productName = "Macbook";
		usernameAdmin = "alex@gmail.com";
		passwordAdmin = "123456";
		usernameClient = "maria@gmail.com";
		passwordClient = "123456";
		client = UserFactory.createClientUser();
		tokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, passwordAdmin);
		tokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, passwordClient);
		invalidToken = tokenAdmin + "xpto";	
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception{
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));			
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception{
		ResultActions result = mockMvc.perform(get("/products?name={productName}", productName)
				.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));			
	}
	
	@Test
	public void insertShouldInsertProductDTOCreatedWhenAdminLogged() throws Exception {
		product = ProductFactory.createProduct();
		product.setId(null);
		ProductDTO productDTO = new ProductDTO(product);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/products")
						.header("Authorization", "Bearer " + tokenAdmin)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
	}

	@Test
	public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndNameInvalid() throws Exception{
		product = ProductFactory.createProduct();
		product.setId(null);
		product.setName("      ");
		ProductDTO productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + tokenAdmin)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
        result.andExpect(jsonPath("$.errors[0].message").value("Campo Requerido."));
	}

    @Test
    public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndDescriptionInvalid() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        product.setDescription("");
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + tokenAdmin)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("description"));
        result.andExpect(jsonPath("$.errors[0].message").value("Descrição precisa ter no minimo 10 caracteres"));
    }

    @Test
    public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndNegativePrice() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        product.setPrice(-3000.0);
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + tokenAdmin)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("price"));
        result.andExpect(jsonPath("$.errors[0].message").value("O preço deve ser positivo"));
    }

    @Test
    public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndPriceIsZero() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        product.setPrice(0.0);
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + tokenAdmin)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("price"));
        result.andExpect(jsonPath("$.errors[0].message").value("O preço deve ser positivo"));
    }

    @Test
    public void insertShouldThrow422WithCustomMessagesWhenAdminLoggedAndDoesNotCategory() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        product.getCategories().clear();
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + tokenAdmin)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("categories"));
        result.andExpect(jsonPath("$.errors[0].message").value("Deve ter pelo menos uma categoria"));
    }

    @Test
    public void insertShouldThrow403WhenClientLogged() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + tokenClient)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldThrow401WhenDoesNotLogged() throws Exception{
        product = ProductFactory.createProduct();
        product.setId(null);
        ProductDTO productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + invalidToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteShouldDeleteExistingProductWhenAdminLogged() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingProductId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());

    }

    @Test
    public void deleteShouldThrow404WhenIdDoesNotExistsAndAdminLogged() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", nonExistingProductId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldThrow400WhenIdIsDependentAndAdminLogged() throws Exception{
    	order = OrderFactory.createOrder(client);
    	for(OrderItem i : order.getItems()) {
    		dependentProductId = i.getProduct().getId();
    	}
        
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", dependentProductId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isBadRequest());

    }

    @Test
    public void deleteShouldThrow403WhenClientLogged() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingProductId)
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isForbidden());
    }

    @Test
    public void deleteShouldThrow401WhenNotLogged() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingProductId)
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isUnauthorized());
    }
}
