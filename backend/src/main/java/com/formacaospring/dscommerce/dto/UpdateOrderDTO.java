package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderStatus;

public class UpdateOrderDTO {
	private Long id;
	private Long clientId;
	private OrderStatus orderStatus;
	
    
    public UpdateOrderDTO() {
    }

	public UpdateOrderDTO(Long id, Long clientId, OrderStatus orderStatus) {
		this.id = id;
		this.clientId = clientId;
		this.orderStatus = orderStatus;
	}
	
	public UpdateOrderDTO(Order entity) {
		id = entity.getId();
		clientId = entity.getClient().getId();
		orderStatus = entity.getStatus();
	}
	
	public Long getId() {
		return id;
	}

	public Long getClientId() {
		return clientId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
}
