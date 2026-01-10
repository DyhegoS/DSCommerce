package com.formacaospring.dscommerce.services;

import com.formacaospring.dscommerce.dto.ProductDTO;
import com.formacaospring.dscommerce.dto.ProductMinDTO;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.repositories.ProductRepository;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;
import com.formacaospring.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingProductId, nonExistingProductId;
    private String productName;
    private Product product;

    private PageImpl<Product> page;

    @BeforeEach
    void setU() throws Exception{
        existingProductId = 1L;
        nonExistingProductId = 2L;
        productName = "PS5";

        product = ProductFactory.createProduct(productName);

        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingProductId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingProductId);
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingProductId);
        });
    }

    @Test
    public void findAllShouldPagedReturnProductMinDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        String name = "PS5";
        Page<ProductMinDTO> result = service.findAll(name, pageable);

        Assertions.assertNotNull(result);
    }
}
