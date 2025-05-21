package es.sasensior.foodex.integration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.CompraPL;

public interface CompraRepository extends JpaRepository<CompraPL, Long>{

    List<CompraPL> findByUsuarioUsername(String username);
	
}
