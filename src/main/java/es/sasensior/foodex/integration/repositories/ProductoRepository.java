package es.sasensior.foodex.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.ProductoPL;

public interface ProductoRepository extends JpaRepository<ProductoPL, Long>{
	
}
