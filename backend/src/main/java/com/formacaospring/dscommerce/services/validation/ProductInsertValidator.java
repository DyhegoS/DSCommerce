package com.formacaospring.dscommerce.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.formacaospring.dscommerce.controllers.handlers.FieldMessage;
import com.formacaospring.dscommerce.dto.CategoryDTO;
import com.formacaospring.dscommerce.dto.product.ProductInsertDTO;
import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.repositories.CategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductInsertValidator implements ConstraintValidator<ProductInsertValid, ProductInsertDTO>{
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize(ProductInsertValid ann) {
    }

    @Override
    public boolean isValid(ProductInsertDTO dto, ConstraintValidatorContext context) {
    	int count = 0;
        List<FieldMessage> list = new ArrayList<>();

        List<Category> category = categoryRepository.findAll();
            
        for(CategoryDTO cat : dto.getCategories()) {
        	boolean existsCategory = category.stream().anyMatch(obj -> obj.getId().equals(cat.getId()));
        	if(!existsCategory) {
        		list.add(new FieldMessage("category"+count, "Categoria ID: "+ cat.getId() + " não existe!"));
        		count++;
        	}
            
        }
       
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
    
}
