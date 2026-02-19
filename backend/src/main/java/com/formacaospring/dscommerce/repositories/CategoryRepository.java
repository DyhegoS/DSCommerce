package com.formacaospring.dscommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.formacaospring.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	@Query("SELECT obj FROM Category obj "
			+ "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	List<Category> searchByName(String name);
}
