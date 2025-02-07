package es.sasensior.foodex.security.services;

import es.sasensior.foodex.security.integration.model.UsuarioPL;

public interface AuthRegisterService {
	
	boolean existsUserByEmail(String email);
	
	boolean existsUserByUsername(String username);
	
	void guardarUsuarioBBDD(UsuarioPL usuario);

}
