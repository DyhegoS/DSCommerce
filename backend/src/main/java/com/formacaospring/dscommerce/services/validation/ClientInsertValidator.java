package com.formacaospring.dscommerce.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.formacaospring.dscommerce.controllers.handlers.FieldMessage;
import com.formacaospring.dscommerce.dto.ClientInsertDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.repositories.ClientRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClientInsertValidator implements ConstraintValidator<ClientInsertValid, ClientInsertDTO>{

    @Autowired
    private ClientRepository repository;
    
    @Override
    public void initialize(ClientInsertValid ann) {
    }

    @Override
    public boolean isValid(ClientInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Client client = repository.findByEmail(dto.getEmail());
        
        if(client != null) {
            list.add(new FieldMessage("email", "E-mail de cliente já existe!"));
        }
        
        client = repository.findByCNPJ(dto.getCnpj());
        if(client != null) {
        	list.add(new FieldMessage("CNPJ", "CNPJ de cliente já existe!"));
        }
        
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
    
}
