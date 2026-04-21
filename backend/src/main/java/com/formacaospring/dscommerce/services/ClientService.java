package com.formacaospring.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscommerce.dto.ClientDTO;
import com.formacaospring.dscommerce.dto.ClientInsertDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;
    
    @Autowired
    private AuthService authService;
    
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(String name, String cnpj, Pageable pageable){
    	String cnpjClear = cnpj.replaceAll("[^0-9]", "");
    	if(name.isEmpty()) {
    		name = null;
    	}else if(cnpjClear.isEmpty()) {
    		cnpjClear = null;
    	}
    	
    	Page<Client> result = repository.searchByNameOrCNPJ(name, cnpjClear, pageable);
    	return result.map(ClientDTO::new);
    }

    
    @Transactional
    public ClientDTO insert(ClientInsertDTO dto) {
    	Client entity = new Client();
    	copyToEntity(entity, dto);  	
    	entity = repository.save(entity);
    	return new ClientDTO(entity);
    }
      
    private void copyToEntity(Client entity, ClientDTO dto) {
    	User user = authService.authenticated();
    	entity.setName(dto.getName());
    	entity.setCnpj(dto.getCnpj());
    	entity.setEmail(dto.getEmail());
    	entity.setAddress(dto.getAddress());
    	entity.setPhone(dto.getPhone());
    	entity.setUser(user);
    }
}
