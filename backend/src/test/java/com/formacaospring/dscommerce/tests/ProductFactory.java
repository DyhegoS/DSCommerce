package com.formacaospring.dscommerce.tests;

import com.formacaospring.dscommerce.dto.ProductDTO;
import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.entities.Product;

public class ProductFactory {
    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product =  new Product(1L, "Xbox One", "Esse é o tal jogo que é melhor que o god of war 4 de usuário", 3000.0, "http://www.microsoft.com");
        product.getCategories().add(category);
        return product;
    }
    public static Product createProduct(String name) {
        Product product = createProduct();
        product.setName(name);
        return product;

    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product);

    }
}
