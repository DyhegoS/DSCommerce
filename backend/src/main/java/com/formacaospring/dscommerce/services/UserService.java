package com.formacaospring.dscommerce.services;

import com.formacaospring.dscommerce.dto.RoleDTO;
import com.formacaospring.dscommerce.dto.UserDTO;
import com.formacaospring.dscommerce.dto.UserInsertDTO;
import com.formacaospring.dscommerce.entities.Role;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.projections.UserDetailsProjection;
import com.formacaospring.dscommerce.repositories.RoleRepository;
import com.formacaospring.dscommerce.repositories.UserRepository;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> users = repository.findAll(pageable);
        return users.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado!"));
        return new UserDTO(user);
    }

    public UserDTO insert(UserInsertDTO dto){
        User entity = new User();
        copyToEntity(entity, dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}

		return user;
	}

	@Transactional(readOnly = true)
	public UserDTO getMe(){
		User user = authService.authenticated();
		return new UserDTO(user);
	}

    private void copyToEntity(User entity, UserDTO dto){
        entity.setName(dto.getName());
        entity.setUsername(dto.getusername());
        entity.setEmail(dto.getEmail());
        entity.getRoles().clear();
        for(RoleDTO roleDto : dto.getRoles()){
            Role role = new Role();
            checkRoleExists(roleDto);
            role.setId(roleDto.getId());
            entity.getRoles().add(role);
        }
    }

    private void checkRoleExists(RoleDTO dto){
        Role role = roleRepository.findById(dto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Role não existe!"));
    }

}
