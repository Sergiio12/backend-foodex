package es.sasensior.foodex.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.CategoriaPL;

public interface CategoriaRepository extends JpaRepository<CategoriaPL, Long> {
	
	boolean existsByNombre(String nombre);
	
    Optional<CategoriaPL> findByNombre(String nombre);

}