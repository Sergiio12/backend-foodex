package es.sasensior.foodex.business.services.impl;

import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;
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
	private DozerBeanMapper mapper;
	
	public CarritoServiceImpl(CarritoRepository carritoRepository, ProductoRepository productoRepository, ItemCarritoRepository itemCarritoRepository, DozerBeanMapper mapper) {
		this.carritoRepository = carritoRepository;
		this.productoRepository = productoRepository;
		this.itemCarritoRepository = itemCarritoRepository;
		this.mapper = mapper;
	}

	@Override
	public Optional<CarritoCompraPL> getCarrito() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();
		
		return this.carritoRepository.findByUsuarioId(usuarioPL.getId());
	}
	
	@Override
	@Transactional
	public void addProductoToCarrito(Long idProducto, int cantidad) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
		
		CarritoCompraPL carrito = this.carritoRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));

		ProductoPL producto = this.productoRepository.findById(idProducto)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
		
		// Verificar si el producto ya está en el carrito
		Optional<ItemCarritoPL> existingItem = this.itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId());
		
		if (existingItem.isPresent()) {
			// Si el producto ya está en el carrito, lanza error:
			throw new IllegalStateException("El producto ya se encuentra en el carrito.");
		} else {
			// Si no está en el carrito, crea una nueva entrada
			ItemCarritoPL itemCarrito = new ItemCarritoPL();
			itemCarrito.setCarrito(carrito);
			itemCarrito.setProducto(producto);
			itemCarrito.setCantidad(cantidad);  // Asigna la cantidad al nuevo producto en el carrito
			this.itemCarritoRepository.save(itemCarrito);
		}
	}

	@Override
	@Transactional
	public void removeProductoFromCarrito(Long idProducto, int cantidad) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
		
		CarritoCompraPL carrito = this.carritoRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));

		ProductoPL producto = this.productoRepository.findById(idProducto)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
		
		// Verificar si el producto está en el carrito
		Optional<ItemCarritoPL> existingItem = this.itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId());
		
		if (existingItem.isPresent()) {
			ItemCarritoPL item = existingItem.get();
			if (item.getCantidad() > cantidad) {
				// Si hay más de la cantidad que queremos eliminar, solo disminuimos la cantidad
				item.setCantidad(item.getCantidad() - cantidad);
				this.itemCarritoRepository.save(item);
			} else {
				// Si la cantidad a eliminar es igual o mayor que la cantidad en el carrito, eliminamos el producto
				this.itemCarritoRepository.delete(item);
			}
		} else {
			throw new EntityNotFoundException("El producto no está en el carrito.");
		}
	}

	@Override
	@Transactional
	public void resetCarritoCompraOfUsuario() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
		
		CarritoCompraPL carrito = this.carritoRepository.findByUsuarioId(usuario.getId())
				.orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));

		// Eliminar todos los items del carrito
		this.itemCarritoRepository.deleteAllByCarrito(carrito.getId());
	}
	
}
