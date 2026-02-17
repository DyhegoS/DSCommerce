package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderDTO {
	private Long id;
	private Long clientId;
    @NotNull(message = "Campo Obrigatório!")
	private OrderStatus status;
	
    
    public UpdateOrderDTO() {
    }

	public UpdateOrderDTO(Long id, Long clientId, OrderStatus status) {
		this.id = id;
		this.clientId = clientId;
		this.status = status;
	}
	
	public UpdateOrderDTO(Order entity) {
		id = entity.getId();
		clientId = entity.getClient().getId();
		status = entity.getStatus();
	}
	
	public Long getId() {
		return id;
	}

	public Long getClientId() {
		return clientId;
	}

	public OrderStatus getStatus() {
		return status;
	}
}
