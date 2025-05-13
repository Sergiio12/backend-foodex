package es.sasensior.foodex.integration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.ProductoPL;

public interface ProductoRepository extends JpaRepository<ProductoPL, Long> {
	
    List<ProductoPL> findByCategoriaId(Long categoriaId);
	
}