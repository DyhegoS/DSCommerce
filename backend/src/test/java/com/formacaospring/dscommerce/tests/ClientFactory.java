package com.formacaospring.dscommerce.tests;



import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.entities.User;

public class ClientFactory {
	
	public static Client createClient() {
		User seller = UserFactory.createAdminUser();
		Client client = new Client(1L, "Empresa Teste", "teste@empresa.com.br", "123456780000199", "Rua Teste, 123", "+551199998888", seller);
		return client;
	}

}
