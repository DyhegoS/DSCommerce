package com.formacaospring.dscommerce.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.repositories.ProductRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public String teste(){
        Optional<Product> result = repository.findById(1L);
        Product product = result.get();
        return product.getName();
    }
}
