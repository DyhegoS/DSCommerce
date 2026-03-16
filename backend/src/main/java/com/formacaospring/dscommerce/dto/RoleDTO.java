package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Role;

public class RoleDTO {
    private Long id;
    private String authority;

    public RoleDTO(){
    }

    public RoleDTO(Long id, String authory) {
        this.id = id;
        this.authority = authory;
    }

    public RoleDTO(Role role){
        id = role.getId();
        authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
