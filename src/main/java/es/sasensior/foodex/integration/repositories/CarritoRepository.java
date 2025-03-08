package es.sasensior.foodex.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.sasensior.foodex.integration.dao.CarritoCompraPL;

public interface CarritoRepository extends JpaRepository<CarritoCompraPL, Long> {
    
    Optional<CarritoCompraPL> findByUsuarioId(Long idUsuario);
    
}
