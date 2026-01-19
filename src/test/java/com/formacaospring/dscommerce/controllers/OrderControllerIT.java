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

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.tests.OrderFactory;
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
	
	long existingOrderId, nonExistingOrderId;
	private String usernameAdmin, usernameClient, passwordAdmin, passwordClient, tokenAdmin, tokenClient, invalidToken;
	private Order order;
	private User admin, client, otherClient;
	
	@BeforeEach
	void setUp() throws Exception{
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;
		usernameAdmin = "alex@gmail.com";
		passwordAdmin = "123456";
		usernameClient = "maria@gmail.com";
		passwordClient = "123456";
		admin = UserFactory.createAdminUser();
		client = UserFactory.createClientUser();
		otherClient = UserFactory.createCustomClientUser(2L, "pangare@gmail.com");
		tokenAdmin = tokenUtil.obtainAccessToken(mockMvc, usernameAdmin, passwordAdmin);
		tokenClient = tokenUtil.obtainAccessToken(mockMvc, usernameClient, passwordClient);
		invalidToken = tokenAdmin + "xpto";		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenAdminLogged() throws Exception{
		Order orderTest = new Order();
		orderTest = OrderFactory.createOrder(admin);
		
		ResultActions result =
                mockMvc.perform(get("/orders/{id}", orderTest.getId())
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(orderTest.getId()));
        result.andExpect(jsonPath("$.moment").value(orderTest.getMoment()));
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
