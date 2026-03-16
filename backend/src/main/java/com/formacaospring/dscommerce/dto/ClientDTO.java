package com.formacaospring.dscommerce.dto;

import com.formacaospring.dscommerce.entities.Client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientDTO {
    private Long id;
    
    @NotBlank(message = "Campo não pode ficar em branco!")
    @Size(min = 2, max = 80, message = "Nome da empresa precisa ter entre 2 a 80 caracteres!")
    private String name;
    
    @Email(message = "Informe um e-mail válido!")
    private String email;

    @Size(min = 10, max = 16, message = "CNPJ precisa ter de 10 a 16 números.")
    @NotNull(message = "Campo requerido!")
    private String cnpj;
    
    @NotBlank(message = "Campo não pode ficar em branco!")
    @Size(min = 5, max = 80, message = "Nome da empresa precisa ter entre 5 a 80 caracteres!")
    private String address;
    
    private String phone;

    public ClientDTO(){
    }
    
    public ClientDTO(Long id) {
    	this.id = id;
    }

    public ClientDTO(Long id, String name, String email, String cnpj, String address, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
        this.address = address;
        this.phone = phone;
    }

    public ClientDTO(Client entity){
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        cnpj = entity.getCnpj();
        address = entity.getAddress();
        phone = entity.getPhone();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
