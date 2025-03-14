package es.sasensior.foodex.integration.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.sasensior.foodex.integration.dao.ItemCarritoIdPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;
import es.sasensior.foodex.integration.dao.ProductoPL;

public interface ItemCarritoRepository extends JpaRepository<ItemCarritoPL, ItemCarritoIdPL> {
    
    @Modifying
    @Query("DELETE FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito AND i.producto.id = :idProducto")
    void removeProductoFromCarrito(Long idCarrito, Long idProducto);
    
    @Modifying
    @Query("DELETE FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito")
    void deleteAllByCarrito(Long idCarrito);

    Optional<ItemCarritoPL> findByCarritoIdAndProductoId(Long idCarrito, Long idProducto);
    
    @Query("SELECT i.producto FROM ItemCarritoPL i WHERE i.carrito.id = :idCarrito")
    List<ProductoPL> findAllProductosByCarrito(@Param("idCarrito") Long idCarrito);
}
