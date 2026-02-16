package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.entities.Product;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;
    
    @Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres.")
    @NotBlank(message = "Campo Requerido.")
    private String name;
    
    @Size(min = 10, message = "Descrição precisa ter no minimo 10 caracteres")  
    @NotBlank(message = "Campo Requerido.")
    private String description;
    
    @NotNull(message = "Campo requerido!")
    @Positive(message = "O preço deve ser positivo")
    private Double price;
    
    @NotNull(message = "Campo requerido!")
    @Positive(message = "A quantidade deve ser positiva")
    private Integer quantity;
    private String imgUrl;

    @NotEmpty(message = "Deve ter pelo menos uma categoria")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, Integer quantity, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        quantity = entity.getQuantity();
        imgUrl = entity.getImgUrl();
        for(Category cat : entity.getCategories()){
            categories.add(new CategoryDTO(cat));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
    
    public Integer getQuantity() {
		return quantity;
	}

	public String getImgUrl() {
        return imgUrl;
    }

     public List<CategoryDTO> getCategories() {
        return categories;
    }
}
