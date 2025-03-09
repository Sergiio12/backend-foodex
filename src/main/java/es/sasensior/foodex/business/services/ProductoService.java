package es.sasensior.foodex.business.services;

import java.util.List;
import java.util.Optional;

import es.sasensior.foodex.business.model.Producto;

public interface ProductoService {
	
	List<Producto> getAll();
	
	Optional<Producto> getProducto(Long idProducto);
	
	void createProducto(Producto producto);
	
	void updateProducto(Producto producto);

}
