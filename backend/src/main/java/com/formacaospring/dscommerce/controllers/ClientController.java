package com.formacaospring.dscommerce.controllers;

import com.formacaospring.dscommerce.dto.ClientDTO;
import com.formacaospring.dscommerce.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

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
}
