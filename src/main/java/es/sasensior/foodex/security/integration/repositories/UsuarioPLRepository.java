package es.sasensior.foodex.security.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.sasensior.foodex.security.integration.model.Rol;
import es.sasensior.foodex.security.integration.model.UsuarioPL;

public interface UsuarioPLRepository extends JpaRepository<UsuarioPL, Long>{

	Optional<UsuarioPL> findByUsername(String username);
	
	@Query("SELECT COUNT(u) > 0 FROM UsuarioPL u WHERE u.email = :email")
	boolean existsByEmail(@Param("email") String email);
	
	@Query("SELECT COUNT(u) > 0 FROM UsuarioPL u WHERE u.username = :username")
	boolean existsByUsername(@Param("username") String name);
	
	@Query("SELECT COUNT(ur) = 1 FROM UsuarioRolesPL ur " +
		       "JOIN ur.usuario u " +
		       "JOIN ur.rol r " +
		       "WHERE u.username = :username " +
		       "AND r.name = 'USUARIO'")
	boolean hasOnlyDefaultRole(@Param("username") String username);
	
	@Query("SELECT COUNT(u) > 0 FROM UsuarioPL u " +
	           "JOIN u.roles r " +
	           "WHERE r.name = :role AND u.username = :username")
	boolean hasRole(@Param("username") String username, @Param("role") Rol role);
	
}
