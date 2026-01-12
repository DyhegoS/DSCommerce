package com.formacaospring.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validateSelfOrAdmin(long userId){
        User me = userService.authenticated();
        if(me.hasRole("ROLE_ADMIN")) {
        	return;
        }
        if(!me.getId().equals(userId)){
        	throw new ForbiddenException("Acesso Negado! Deve ser dono do pedido ou admin!");
        }
    }
}
