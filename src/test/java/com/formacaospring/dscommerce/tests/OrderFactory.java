package com.formacaospring.dscommerce.tests;

import java.time.Instant;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;
import com.formacaospring.dscommerce.entities.Payment;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;

public class OrderFactory {
	public static Order createOrder(User client) {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		return order;
	}

}
