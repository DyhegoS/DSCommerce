package com.formacaospring.dscommerce.repositories;

import com.formacaospring.dscommerce.dto.OrderMinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.formacaospring.dscommerce.entities.Order;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>{

    @Query("SELECT new com.formacaospring.dscommerce.dto.OrderMinDTO(obj) " +
            "FROM Order obj")
    Page<OrderMinDTO> searchAll(Pageable pageable);
}
