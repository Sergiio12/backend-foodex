package es.sasensior.foodex.business.services;

import java.util.Optional;

import es.sasensior.foodex.integration.dao.CarritoCompraPL;

public interface CarritoService {
	
	Optional<CarritoCompraPL> getCarrito();
	
	void addProductoToCarrito(Long idProducto, int cantidad);
	
	void removeProductoFromCarrito(Long idProducto, int cantidad);
	
	void resetCarritoCompraOfUsuario();
	
}
