package es.sasensior.foodex.security.services;

import java.util.Optional;

import es.sasensior.foodex.security.integration.dao.RolPL;

public interface RolPLService {

	Optional<RolPL> getRol(String nombreRol);
	
}
