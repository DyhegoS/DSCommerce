package com.formacaospring.dscommerce.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacaospring.dscommerce.dto.OrderDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.tests.ClientFactory;
import com.formacaospring.dscommerce.tests.ProductFactory;
import com.formacaospring.dscommerce.tests.TokenUtil;
import com.formacaospring.dscommerce.tests.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String userSellerUsername, otherUserSellerUsername, userSellerPassword, otherUserSellerPassword, adminUsername, adminPassword;
	private String userSellerToken, otherUserSellerToken, adminToken, invalidToken;
	private Long existingOrderId, nonExistingOrderId;
	
	private Order order;
	private OrderDTO orderDTO;
	private User user;
	private Client client;
	
	@BeforeEach
	void setUp() throws Exception {
		
		userSellerUsername = "jaja24@gmail.com";
		userSellerPassword = "123456";
		otherUserSellerUsername = "alex@gmail.com";
		otherUserSellerPassword = "123456";
		adminUsername = "admin@gmail.com";
		adminPassword = "123456";
		client = ClientFactory.createClient();
		
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;
		
		userSellerToken = tokenUtil.obtainAccessToken(mockMvc, userSellerUsername, userSellerPassword);
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		otherUserSellerToken = tokenUtil.obtainAccessToken(mockMvc, otherUserSellerUsername, otherUserSellerPassword);
		invalidToken = adminToken + "xpto"; // Simulates a wrong token
		
		user = UserFactory.createSellerUser();
		order = new Order(null, Instant.now(), null, OrderStatus.WAITING_APPROVAL, user, user, null, client);
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(order);
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", existingOrderId)
					.header("Authorization", "Bearer " + adminToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(1L));
		result.andExpect(jsonPath("$.moment").value("2026-01-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("SEPARATION"));
		result.andExpect(jsonPath("$.user").exists());
		result.andExpect(jsonPath("$.userUpdate").exists());
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndUserSellerLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", existingOrderId)
					.header("Authorization", "Bearer " + userSellerToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.moment").value("2026-01-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("SEPARATION"));
		result.andExpect(jsonPath("$.user").exists());
		result.andExpect(jsonPath("$.userUpdate").exists());
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenIdExistsAndOtherUserSellerLogged() throws Exception {
		
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", existingOrderId)
					.header("Authorization", "Bearer " + otherUserSellerToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
					.header("Authorization", "Bearer " + adminToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndUserSellerLogged() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
					.header("Authorization", "Bearer " + userSellerToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}	
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/orders/{id}", existingOrderId)
					.header("Authorization", "Bearer " + invalidToken)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void insertShouldReturnOrderDTOCreatedWhenUserSellerLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + userSellerToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.moment").exists());
		result.andExpect(jsonPath("$.updateMoment").value(is(nullValue())));
		result.andExpect(jsonPath("$.status").value("WAITING_APPROVAL"));
		result.andExpect(jsonPath("$.user").exists());
		result.andExpect(jsonPath("$.userUpdate").exists());
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenClientLoggedAndOrderHasNoItem() throws Exception {
		
		orderDTO.getItems().clear();

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + userSellerToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenAdminLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + adminToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + invalidToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
}
