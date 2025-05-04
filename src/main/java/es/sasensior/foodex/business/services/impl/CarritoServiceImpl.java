package es.sasensior.foodex.business.services.impl;

import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.CarritoCompra;
import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CarritoRepository;
import es.sasensior.foodex.integration.repositories.ItemCarritoRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Service
public class CarritoServiceImpl implements CarritoService {
        
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private DozerBeanMapper mapper;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public CarritoServiceImpl(CarritoRepository carritoRepository, ProductoRepository productoRepository, ItemCarritoRepository itemCarritoRepository, DozerBeanMapper mapper, EntityManager entityManager) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
    }
    
    @Override
    public Optional<CarritoCompra> getCarrito() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();

        return this.carritoRepository.findByUsuarioId(usuarioPL.getId())
            .map(carrito -> mapper.map(carrito, CarritoCompra.class));
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
        
        Optional<ItemCarritoPL> existingItem = this.itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId());
        
        if (existingItem.isPresent()) {
            throw new IllegalStateException("El producto ya se encuentra en el carrito.");
        } else {
            if (producto.getStock() < cantidad) {
                throw new IllegalStateException("No hay suficiente cantidad de stock disponible para comprar ese producto.");
            }
            ItemCarritoPL itemCarrito = new ItemCarritoPL();
            itemCarrito.setCarrito(carrito);
            itemCarrito.setProducto(producto);
            itemCarrito.setCantidad(cantidad);
            this.itemCarritoRepository.save(itemCarrito);
        }
    }
    
    @Override
    @Transactional
    public Optional<CarritoCompra> modifyProductoInCarrito(Long idProducto, int nuevaCantidad) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
        
        CarritoCompraPL carrito = carritoRepository.findByUsuarioId(usuario.getId())
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));

        ProductoPL producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        ItemCarritoPL item = itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), producto.getId())
            .orElseThrow(() -> new EntityNotFoundException("El producto no está en el carrito"));

        if (nuevaCantidad <= 0) {
            itemCarritoRepository.delete(item);
        } else {
            if (producto.getStock() < nuevaCantidad) {
                throw new IllegalStateException("No hay suficiente stock disponible");
            }
            item.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(item);
        }

        return carritoRepository.findByUsuarioId(usuario.getId())
            .map(carritoPL -> mapper.map(carritoPL, CarritoCompra.class));
    }

    @Override
    @Transactional
    public Optional<CarritoCompra> removeProductoFromCarrito(Long idProducto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
        
        CarritoCompraPL carrito = carritoRepository.findByUsuarioId(usuario.getId())
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));
        
        ItemCarritoPL item = carrito.getItemsCarrito().stream()
            .filter(i -> i.getProducto().getId().equals(idProducto))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Producto no está en el carrito"));

        carrito.getItemsCarrito().remove(item);
        itemCarritoRepository.delete(item);

        carritoRepository.flush();
        entityManager.refresh(carrito);

        return Optional.of(mapper.map(carrito, CarritoCompra.class));
    }

    @Override
    @Transactional
    public Optional<CarritoCompra> removeAllProductosFromCarrito() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPL usuario = (UsuarioPL) authentication.getPrincipal();
        
        CarritoCompraPL carrito = carritoRepository.findByUsuarioId(usuario.getId())
            .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado"));

        itemCarritoRepository.deleteAllByCarrito(carrito.getId());

        return carritoRepository.findByUsuarioId(usuario.getId())
            .map(carritoPL -> mapper.map(carritoPL, CarritoCompra.class));
    }
}