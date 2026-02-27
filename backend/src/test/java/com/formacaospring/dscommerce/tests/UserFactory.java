package com.formacaospring.dscommerce.tests;

import com.formacaospring.dscommerce.entities.Role;
import com.formacaospring.dscommerce.entities.User;

public class UserFactory {
	public static User createSellerUser() {
		User user = new User(2L, "Alex","alex@gmail.com", "alex@gmail.com", "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(3L, "ROLE_SELLER"));		
		return user;
	}
	
	public static User createStockUser() {
		User user = new User(3L, "Maria Brown","maria@gmail.com", "maria@gmail.com", "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(1L, "ROLE_USER_STOCK"));		
		return user;
	}
	
	public static User createAdminUser() {
		User user = new User(1L, "admin","admin@gmail.com", "admin@gmail.com", "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
		user.addRole(new Role(2L, "ROLE_ADMIN"));		
		return user;
	}
	
}
