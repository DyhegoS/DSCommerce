package com.formacaospring.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.formacaospring.dscommerce.dto.OrderDTO;
import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.tests.OrderFactory;
import com.formacaospring.dscommerce.tests.ProductFactory;
import com.formacaospring.dscommerce.tests.TokenUtil;
import com.formacaospring.dscommerce.tests.UserFactory;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private Long existingOrderId, nonExistingOrderId;
	private String usernameAdmin, usernameClient, passwordAdmin, passwordClient, tokenAdmin, tokenClient, invalidToken;
	private Order order;
	private OrderDTO orderDTO;
	private User user;
	
	@BeforeEach
	void setUp() throws Exception{
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;
		
		usernameAdmin = "alex@gmail.com";
		passwordAdmin = "123456";
		usernameClient = "maria@gmail.com";
		passwordClient = "123456";
		
		user = UserFactory.createClientUser();
		order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		tokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, passwordAdmin);
		tokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, passwordClient);
		invalidToken = tokenAdmin + "xpto";		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception{
		
		ResultActions result =
                mockMvc.perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingOrderId));
        result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenClientLogged() throws Exception{
		Order orderTest = new Order();
		orderTest = OrderFactory.createOrder(client);
		ResultActions result =
                mockMvc.perform(get("/orders/{id}", orderTest.getId())
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(orderTest.getId()));
        result.andExpect(jsonPath("$.moment").value(orderTest.getMoment()));
	}

}
