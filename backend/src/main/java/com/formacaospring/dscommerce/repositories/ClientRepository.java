package com.formacaospring.dscommerce.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.formacaospring.dscommerce.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT obj FROM Client obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Client> searchByName(String name, Pageable pageable);

    @Query("SELECT obj FROM Client obj " +
            "WHERE obj.cnpj LIKE CONCAT('%', :cnpj, '%')")
    Page<Client> searchByCnpj(String cnpj, Pageable pageable);
    
    Client findByEmail(String email);
    
    @Query("SELECT obj FROM Client obj "
    		+ "WHERE obj.cnpj = :cnpj")
    Client findByCNPJ(String cnpj);
}
