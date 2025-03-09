package es.sasensior.foodex.business.services;

import java.util.List;
import java.util.Optional;

import es.sasensior.foodex.business.model.Compra;
import es.sasensior.foodex.business.model.ItemCarrito;

public interface CompraService {
	
	List<Compra> getAllCompras();
	
	Optional<Compra> getCompra();
	
	void removeCompra();
	
	void comprar(List<ItemCarrito> itemsCarrito);

}
