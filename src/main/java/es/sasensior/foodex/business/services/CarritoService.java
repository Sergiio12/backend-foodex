package es.sasensior.foodex.business.services;

import java.util.Optional;

import es.sasensior.foodex.business.model.CarritoCompra;

public interface CarritoService {
	
	Optional<CarritoCompra> getCarrito();
	
	void addProductoToCarrito(Long idProducto, int cantidad);
	
	void removeProductoFromCarrito(Long idProducto, int cantidad);
	
	void removeAllProductosFromCarrito();
	
}
