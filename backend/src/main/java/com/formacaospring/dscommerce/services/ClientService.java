package com.formacaospring.dscommerce.services;

import com.formacaospring.dscommerce.dto.ClientDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.repositories.ClientRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;
    
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
    	Page<Client> result = repository.findAll(pageable);
    	return result.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findByName(String name, Pageable pageable){
        Page<Client> result = repository.searchByName(name, pageable);
        return result.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ClientDTO> findByCnpj(String cnpj, Pageable pageable){
    	String cnpjClear = cnpj.replaceAll("[^0-9]", "");
        String regexCNPJ = "^(?!(\\d)\\1{13})\\d{14}$";
        if(cnpjClear.matches(regexCNPJ)){
            Page<Client> result = repository.searchByCnpj(cnpjClear, pageable);
            return result.map(ClientDTO::new);
        }else{
            throw new IllegalArgumentException("número de CNPJ inválido ou não existe!");
        }
    }
}
