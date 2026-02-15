package com.formacaospring.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
