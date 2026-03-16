package com.formacaospring.dscommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.formacaospring.dscommerce.dto.ClientDTO;
import com.formacaospring.dscommerce.dto.ClientInsertDTO;
import com.formacaospring.dscommerce.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;
    
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(@RequestParam(defaultValue = "") String name,
                                                   @RequestParam(defaultValue = "") String cnpj,
                                                   Pageable pageable){
        if(!name.isBlank() && cnpj.isBlank()){
            Page<ClientDTO> dto = service.findByName(name, pageable);
            return ResponseEntity.ok(dto);
        }else if(name.isBlank() && !cnpj.isBlank()) {
        	Page<ClientDTO> dto = service.findByCnpj(cnpj, pageable);
            return ResponseEntity.ok(dto);
        }

        Page<ClientDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping
    public ResponseEntity<ClientDTO> insert(@Valid @RequestBody ClientInsertDTO dto){
    	ClientDTO client = service.insert(dto);
    	URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
    	return ResponseEntity.created(uri).body(client);
    }
}
