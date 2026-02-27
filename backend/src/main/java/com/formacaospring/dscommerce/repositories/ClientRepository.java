package com.formacaospring.dscommerce.repositories;

import com.formacaospring.dscommerce.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT obj FROM Client obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Client> searchByName(String name, Pageable pageable);

    @Query("SELECT obj FROM Client obj " +
            "WHERE obj.cnpj LIKE CONCAT('%', :cnpj, '%')")
    Page<Client> searchByCnpj(String cnpj, Pageable pageable);
}
