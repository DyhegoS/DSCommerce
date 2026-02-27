package com.formacaospring.dscommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscommerce.repositories.CategoryRepository;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import com.formacaospring.dscommerce.dto.CategoryDTO;
import com.formacaospring.dscommerce.entities.Category;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }
    
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
    	Category category = repository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException("ID de categoria não encontrado!"));
    	return new CategoryDTO(category);
    }
    
    @Transactional(readOnly = true)
    public List<CategoryDTO> findByName(String name) {
    	List<Category> category = repository.searchByName(name);
    	return category.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
    
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
    	Category entity = new Category();
    	entity.setName(dto.getName());
    	entity = repository.save(entity);
    	return new CategoryDTO(entity);
    }
    
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
    	try {
    		Category entity = repository.getReferenceById(id);
    		entity.setName(dto.getName());
    		entity = repository.save(entity);
    		return new CategoryDTO(entity);
    	}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("id de categoria não encontrado!");
		}
    }
}
