package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Role;

public class RoleDTO {
    private Long id;
    private String authory;

    public RoleDTO(){
    }

    public RoleDTO(Long id, String authory) {
        this.id = id;
        this.authory = authory;
    }

    public RoleDTO(Role role){
        id = role.getId();
        authory = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthory() {
        return authory;
    }
}
