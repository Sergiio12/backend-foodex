package es.sasensior.foodex.business.services;

public interface CarritoService {
	
	void addProductoToCarrito(Long idProducto, int cantidad);
	
	void removeProductoFromCarrito(Long idProducto, int cantidad);
	
	void resetCarritoCompraOfUsuario();
	
}
