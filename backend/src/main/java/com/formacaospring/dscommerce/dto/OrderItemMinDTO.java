package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;

public class OrderItemMinDTO {

    private Long id;

    public OrderItemMinDTO(){
    }

    public OrderItemMinDTO(OrderItem entity) {
        for(OrderItem item : entity.getOrder().getItems()){
            this.id = item.getProduct().getId();
        }
    }

    public Long getId() {
        return id;
    }
}
