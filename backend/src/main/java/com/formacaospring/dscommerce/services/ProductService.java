package com.formacaospring.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscommerce.dto.CategoryDTO;
import com.formacaospring.dscommerce.dto.product.ProductDTO;
import com.formacaospring.dscommerce.dto.product.ProductInsertDTO;
import com.formacaospring.dscommerce.entities.Category;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.projections.ProductProjection;
import com.formacaospring.dscommerce.repositories.CategoryRepository;
import com.formacaospring.dscommerce.repositories.ProductRepository;
import com.formacaospring.dscommerce.services.exceptions.DatabaseException;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;
import com.formacaospring.dscommerce.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name, String categoryName, Pageable pageable) {
    	
    	List<Category> catNames = categoryRepository.searchByName(categoryName);
    	List<Long> categoryIds = catNames.stream().map(x -> x.getId()).toList();
    			 			
		Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);
		
        List<Long> productIds = page.map(x -> x.getId()).toList();

        List<Product> entities = repository.searchProductWithCategories(productIds);
        entities = (List<Product>) Utils.replace(page.getContent(), entities);
        List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p, p.getCategories())).toList();

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
	}

    @Transactional(readOnly = true)
    public Page<ProductDTO> findByMinAndMaxPrice(String min, String max, Pageable pageable){
        try{
            Double minPrice = Double.parseDouble(min);
            Double maxPrice = Double.parseDouble(max);
            Page<Product> result = repository.searchByMinAndMaxPrice(minPrice, maxPrice, pageable);
            return result.map(ProductDTO::new);
        }catch(NumberFormatException e) {
            throw new NumberFormatException("Favor informa valores válidos!");
        }
    }

    @Transactional
    public ProductDTO insert(ProductInsertDTO dto) {
        Product entity = new Product();
        copyDtoToentity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToentity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }
        try{
            repository.deleteById(id);
        }
        catch(DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial!");
        }
    }

    private void copyDtoToentity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setImgUrl(dto.getImgUrl());
        entity.getCategories().clear();
        for(CategoryDTO catDto : dto.getCategories()){
            Category cat = new Category();
            cat.setId(catDto.getId());
            entity.getCategories().add(cat);
        }
    }
}
