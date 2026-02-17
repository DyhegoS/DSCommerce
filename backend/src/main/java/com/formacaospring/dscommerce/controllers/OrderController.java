package com.formacaospring.dscommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.formacaospring.dscommerce.dto.OrderDTO;
import com.formacaospring.dscommerce.dto.UpdateOrderDTO;
import com.formacaospring.dscommerce.services.OrderService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping
    public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping (value = "/{id}")
    public ResponseEntity<UpdateOrderDTO> update(@PathVariable Long id,@Valid @RequestBody UpdateOrderDTO dto){
    	dto = service.update(id, dto);
    	return ResponseEntity.ok(dto);
    }
}
