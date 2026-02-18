package com.formacaospring.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.services.exceptions.ForbiddenException;
import com.formacaospring.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

	@InjectMocks
	private AuthService service;

	@Mock
	private UserService userService;

	private User admin, userSeller, UserStock;

	@BeforeEach
	void setUp() throws Exception {
		admin = UserFactory.createAdminUser();
		userSeller = UserFactory.createSellerUser();
		UserStock = UserFactory.createStockUser();
	}

	@Test
	public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {
		Mockito.when(userService.authenticated()).thenReturn(admin);

		Long userId = admin.getId();

		Assertions.assertDoesNotThrow(() -> {
			service.validateSelfOrAdmin(userId);
		});
	}

	@Test
	public void validateSelfOrAdminShouldDoNothingWhenSellerLogged() {
		Mockito.when(userService.authenticated()).thenReturn(userSeller);

		Long userId = userSeller.getId();

		Assertions.assertDoesNotThrow(() -> {
			service.validateSelfOrAdmin(userId);
		});
	}
	
	@Test
	public void validateSelfOrAdminShouldThrowsForbiddenExceptionWhenUserStockLogged() {
		Mockito.when(userService.authenticated()).thenReturn(UserStock);
		
		Long userId = userSeller.getId();
		
		Assertions.assertThrows(ForbiddenException.class, () -> {
			service.validateSelfOrAdmin(userId);
		});
		
	}

}
