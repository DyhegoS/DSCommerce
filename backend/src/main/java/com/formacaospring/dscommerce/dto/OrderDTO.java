package com.formacaospring.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private Instant updateMoment;
    private OrderStatus status;

    private UserDTO user;
    private UserDTO userUpdate;
    private ClientDTO client;

    private PaymentDTO payment;

    @NotEmpty(message = "Deve ter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(){
    }

    public OrderDTO(Long id, Instant moment, Instant updateMoment, OrderStatus status, UserDTO user, UserDTO userUpdate, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.user = user;
        this.userUpdate = userUpdate;
        this.client = client;
        this.payment = payment;
    }

     public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        updateMoment = entity.getUpdateMoment();
        status = entity.getStatus();
        user = new UserDTO(entity.getUser());
        userUpdate = new UserDTO(entity.getUserUpdate());
        client = new ClientDTO(entity.getClient());
        payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
        for(OrderItem item : entity.getItems()){
            OrderItemDTO itemDto = new OrderItemDTO(item);
            items.add(itemDto);
        }
    }

     public Long getId() {
         return id;
     }

     public Instant getMoment() {
         return moment;
     }

    public Instant getUpdateMoment() {
        return updateMoment;
    }

    public OrderStatus getStatus() {
         return status;
     }

     public UserDTO getUser() {
         return user;
     }

    public UserDTO getUserUpdate() {
        return userUpdate;
    }

    public ClientDTO getClient() {
		return client;
	}

	 public PaymentDTO getPayment() {
         return payment;
     }

     public List<OrderItemDTO> getItems() {
         return items;
     }   

     public Double getTotal(){
        double sum = 0.0;
        for(OrderItemDTO itemsDTO : items){
            sum += itemsDTO.getSubtotal();
        }
        return sum;
     }
}
