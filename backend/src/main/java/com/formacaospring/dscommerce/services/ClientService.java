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
