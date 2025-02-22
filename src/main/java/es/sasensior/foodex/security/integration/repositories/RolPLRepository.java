package es.sasensior.foodex.security.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.sasensior.foodex.security.integration.dao.RolPL;

public interface RolPLRepository extends JpaRepository<RolPL, Long>{

	@Query("SELECT r FROM RolPL r WHERE UPPER(r.name) = UPPER(:name)")
	Optional<RolPL> findByName(@Param("name") String name);
	
}
