package com.formacaospring.dscommerce.services;

import com.formacaospring.dscommerce.repositories.UserRepository;
import com.formacaospring.dscommerce.util.CustomUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserUtil customUserUtil;

    public void validateSelfOrAdmin(Long userId){
        User me = authenticated();
        if(me.hasRole("ROLE_ADMIN")) {
        	return;
        }
        if(!me.getId().equals(userId)){
        	throw new ForbiddenException("Acesso Negado! Deve ser dono do pedido ou admin!");
        }
    }
    
    public void validateAdmin(Long id) {
    	User me = authenticated();
    	if(me.hasRole("ROLE_ADMIN") && me.getId().equals(id)) {
    		throw new ForbiddenException("Acesso Negado! Não se pode auto excluir!");
    	}
    }

    protected User authenticated() {
        try{
            String username = customUserUtil.getLoggedUsername();
            return userRepository.findByEmail(username);
        }
        catch(Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }
}
