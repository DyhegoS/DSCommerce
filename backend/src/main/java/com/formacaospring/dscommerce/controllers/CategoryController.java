package com.formacaospring.dscommerce.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.formacaospring.dscommerce.dto.CategoryDTO;
import com.formacaospring.dscommerce.services.CategoryService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER_STOCK')")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(@RequestParam (defaultValue = "") String name){
    	if(!name.isEmpty()) {
    		List<CategoryDTO> list = service.findByName(name);
    		return ResponseEntity.ok(list);
    	}
        List<CategoryDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_STOCK')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
    	CategoryDTO dto = service.findById(id);
    	return ResponseEntity.ok(dto);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_STOCK')")
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto){
    	dto = service.insert(dto);
    	URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
    	return ResponseEntity.created(uri).body(dto);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_STOCK')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto){
    	dto = service.update(id, dto);
    	return ResponseEntity.ok(dto);
    }

}
