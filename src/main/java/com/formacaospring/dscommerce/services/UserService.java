package com.formacaospring.dscommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.formacaospring.dscommerce.entities.Role;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.projections.UserDetailsProjection;
import com.formacaospring.dscommerce.repositories.UserRepository;

public class UserService implements UserDetailsService{
    private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for(UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		
		return user;
	}
    
}
