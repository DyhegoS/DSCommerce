package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderDTO {
	private Long id;
    private UserDTO userUpdate;
	private Long clientId;

    @NotNull(message = "Campo Obrigatório!")
	private OrderStatus status;
	
    
    public UpdateOrderDTO() {
    }

	public UpdateOrderDTO(Long id, UserDTO userUpdate, Long clientId, OrderStatus status) {
		this.id = id;
        this.userUpdate = userUpdate;
		this.clientId = clientId;
		this.status = status;
	}
	
	public UpdateOrderDTO(Order entity) {
		id = entity.getId();
        userUpdate = new UserDTO(entity.getUserUpdate());
		clientId = entity.getClient().getId();
		status = entity.getStatus();
	}
	
	public Long getId() {
		return id;
	}

    public UserDTO getUserUpdate(){ return userUpdate; }

	public Long getClientId() {
		return clientId;
	}

	public OrderStatus getStatus() {
		return status;
	}
}
