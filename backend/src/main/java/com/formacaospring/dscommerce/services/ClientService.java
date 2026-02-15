package com.formacaospring.dscommerce.services;

import com.formacaospring.dscommerce.dto.ClientDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    public Page<ClientDTO> findAll(String name, Pageable pageable){
        Page<Client> result = repository.searchByName(name, pageable);
        return result.map(ClientDTO::new);
    }

    public Page<ClientDTO> findByCnpj(String cnpj, Pageable pageable){
        String regexCNPJ = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$";
        if(cnpj.matches(regexCNPJ)){
            Page<Client> result = repository.searchByCnpj(cnpj, pageable);
            return result.map(ClientDTO::new);
        }else{
            throw new IllegalArgumentException("número de CNPJ inválido!");
        }
    }
}
