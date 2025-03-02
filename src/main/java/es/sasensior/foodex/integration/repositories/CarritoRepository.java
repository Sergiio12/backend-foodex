package es.sasensior.foodex.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.sasensior.foodex.integration.dao.CarritoCompraPL;

public interface CarritoRepository extends JpaRepository<CarritoCompraPL, Long> {
    
    Optional<CarritoCompraPL> findByUsuarioId(Long idUsuario);

    @Modifying
    @Query("DELETE FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito AND i.producto.id = :idProducto")
    void removeProductoFromCarrito(Long idCarrito, Long idProducto);
}
