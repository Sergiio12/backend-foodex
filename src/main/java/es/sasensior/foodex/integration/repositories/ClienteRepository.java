package es.sasensior.foodex.integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.ClientePL;

public interface ClienteRepository extends JpaRepository<ClientePL, Long>{
	
}
