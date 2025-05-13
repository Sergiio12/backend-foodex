package es.sasensior.foodex.business.services;

import java.util.List;
import java.util.Optional;

import es.sasensior.foodex.business.model.Producto;

public interface ProductoService {
	
	List<Producto> getAll();
	
	List<Producto> getProductosByCategoria(Long idCategoria);
	
	Optional<Producto> getProducto(Long idProducto);
	
	Producto createProducto(Producto producto);
	
	void deleteProducto(Long idProducto);
	
	Producto updateProducto(Producto producto);

}
