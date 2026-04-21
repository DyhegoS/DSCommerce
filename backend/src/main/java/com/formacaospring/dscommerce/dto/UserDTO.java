package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    
    private Long id;
    @Size(min = 2, max = 50, message = "Campo deve ter no minimo 2 caracteres e no máximo 50")
    @NotBlank(message = "Campo não pode ficar em branco")
    private String name;

    private String username;

    @Email(message = "Informar e-mail válido!")
	private String email;

    private Set<RoleDTO> roles = new HashSet<>();
    
    public UserDTO() {
    }

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        username = entity.getUsername();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
		return name;
	}

	public String getusername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
