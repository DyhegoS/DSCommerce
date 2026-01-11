package com.formacaospring.dscommerce.tests;

import com.formacaospring.dscommerce.entities.Role;
import com.formacaospring.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {
    public static User createClientUser(){
        User user = new User(1L, "Ana", "ana@gmail.com", "11987651234",
                LocalDate.parse("2001-07-25"),"$2a$10$RsB5BPHXdY2eRotNKWkhv.Wg2wshgsVVNi6OQNSmrbRUGEUd8bMtS");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser(){
        User user = new User(2L, "Mike", "mike@gmail.com", "11987651234",
                LocalDate.parse("2001-07-25"),"$2a$10$RsB5BPHXdY2eRotNKWkhv.Wg2wshgsVVNi6OQNSmrbRUGEUd8bMtS");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username){
        User user = new User(id, username, username, "11987651234",
                LocalDate.parse("2001-07-25"),"$2a$10$RsB5BPHXdY2eRotNKWkhv.Wg2wshgsVVNi6OQNSmrbRUGEUd8bMtS");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username){
        User user = new User(id, username, username, "11987651234",
                LocalDate.parse("2001-07-25"),"$2a$10$RsB5BPHXdY2eRotNKWkhv.Wg2wshgsVVNi6OQNSmrbRUGEUd8bMtS");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
}
