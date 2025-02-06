package es.sasensior.foodex.security.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.security.integration.model.UsuarioPL;

public interface UsuarioPLRepository extends JpaRepository<UsuarioPL, Long>{

	Optional<UsuarioPL> findByUsername(String username);
	
}
