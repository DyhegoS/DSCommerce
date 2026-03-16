package com.formacaospring.dscommerce.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.formacaospring.dscommerce.controllers.handlers.FieldMessage;
import com.formacaospring.dscommerce.dto.UserInsertDTO;
import com.formacaospring.dscommerce.entities.Role;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.repositories.RoleRepository;
import com.formacaospring.dscommerce.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO>{

    @Autowired
    private UserRepository repository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail());
        Optional<Role> role = roleRepository.findById(dto.getRoles().iterator().next().getId());
        
        if(user != null) {
            list.add(new FieldMessage("email", "E-mail já existe!"));
        }
        
        if(role.isEmpty()) {
        	list.add(new FieldMessage("role", "Role não existe!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
    
}
