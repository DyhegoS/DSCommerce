package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    private Long id;
    
    @Size(min = 2, max = 20, message = "Campo precisa ter entre 2 a 20 caracteres!")
    @NotNull(message = "Campo Obrigatório!")
    private String name;

    public CategoryDTO(){
    }
    
    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
