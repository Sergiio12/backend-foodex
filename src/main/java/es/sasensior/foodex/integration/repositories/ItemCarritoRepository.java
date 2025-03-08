package es.sasensior.foodex.integration.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import es.sasensior.foodex.integration.dao.ItemCarritoIdPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;

public interface ItemCarritoRepository extends JpaRepository<ItemCarritoPL, ItemCarritoIdPL> {
    
    @Modifying
    @Query("DELETE FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito AND i.producto.id = :idProducto")
    void removeProductoFromCarrito(Long idCarrito, Long idProducto);
    
    @Modifying
    @Query("DELETE FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito")
    void deleteAllByCarrito(Long idCarrito);

    Optional<ItemCarritoPL> findByCarritoIdAndProductoId(Long idCarrito, Long idProducto);
}
