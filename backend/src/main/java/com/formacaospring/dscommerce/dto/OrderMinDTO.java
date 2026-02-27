package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.*;

import java.util.ArrayList;
import java.util.List;

public class OrderMinDTO {
    private Long id;
    private OrderStatus orderStatus;
    private Long userId;
    private Long clientId;
    private PaymentStatus paymentStatus;
    private List<OrderItemMinDTO> items = new ArrayList<>();

    public OrderMinDTO(){
    }

    public OrderMinDTO(Long id, OrderStatus orderStatus, Long userId, Long clientId, PaymentStatus paymentStatus) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.clientId = clientId;
        this.paymentStatus = paymentStatus;
    }

    public OrderMinDTO(Order entity) {
        id = entity.getId();
        orderStatus = entity.getStatus();
        userId = entity.getUser().getId();
        clientId = entity.getClient().getId();
        paymentStatus = entity.getPayment().getStatus();
        for(OrderItem orderItem : entity.getItems()){
            items.add(new OrderItemMinDTO(orderItem));
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getClientId() {
        return clientId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public List<OrderItemMinDTO> getItemsId() {
        return items;
    }
}
