package com.formacaospring.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formacaospring.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
