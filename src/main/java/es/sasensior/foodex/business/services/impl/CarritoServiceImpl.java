package es.sasensior.foodex.business.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.integration.dao.ItemCarritoIdPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CarritoRepository;
import es.sasensior.foodex.integration.repositories.ItemCarritoRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Service
public class CarritoServiceImpl implements CarritoService {
	
	private final CarritoRepository carritoRepository;
	private final ItemCarritoRepository itemCarritoRepository;
	private final ProductoRepository productoRepository;
	
	public CarritoServiceImpl(CarritoRepository carritoRepository, ProductoRepository productoRepository, ItemCarritoRepository itemCarritoRepository) {
		this.carritoRepository = carritoRepository;
		this.productoRepository = productoRepository;
		this.itemCarritoRepository = itemCarritoRepository;
	}

	@Override
	@Transactional
	public void addProductoToCarrito(Long idProducto, int cantidad) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
		
		CarritoCompraPL carrito = carritoRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));

		ProductoPL producto = productoRepository.findById(idProducto)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
		
		//Creamos clave compuesta:
		
		ItemCarritoIdPL itemCarritoId = new ItemCarritoIdPL();
		itemCarritoId.setCarrito(carrito.getId());
		itemCarritoId.setProducto(producto.getId());
		
		//Creamos y guardamos nuevo producto en el carrito:
		
		//TODO
		
	}

	@Override
	public void removeProductoFromCarrito(Long idProducto, int cantidad) {
		//TODO	
	}

	@Override
	public void resetCarritoCompraOfUsuario() {
		//TODO
	}

	

}
