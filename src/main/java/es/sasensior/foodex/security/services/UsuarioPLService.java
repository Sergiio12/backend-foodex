package es.sasensior.foodex.security.services;

import es.sasensior.foodex.security.integration.model.UsuarioPL;

public interface UsuarioPLService {
	
	boolean existsUserByUsername(String username);
	
	boolean existsUserByEmail(String email);
	
	void register(UsuarioPL usuario);
	
	void addRol(String username, String rolName);
	
	void removeRol(String username, String rolName);

}
