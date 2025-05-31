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

/**
 * Implementación del servicio para la gestión del carrito de compra.
 * Proporciona funcionalidades para manipular el carrito de compra de un usuario,
 * incluyendo añadir, modificar y eliminar productos.
 */
@Getter
@Service
public class CarritoServiceImpl implements CarritoService {
        
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private DozerBeanMapper mapper;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Constructor para la inyección de dependencias.
     *
     * @param carritoRepository Repositorio para operaciones con carritos
     * @param productoRepository Repositorio para operaciones con productos
     * @param itemCarritoRepository Repositorio para operaciones con items del carrito
     * @param mapper Mapeador para conversión entre entidades y modelos
     * @param entityManager Gestor de entidades JPA
     */
    public CarritoServiceImpl(CarritoRepository carritoRepository, ProductoRepository productoRepository, 
                             ItemCarritoRepository itemCarritoRepository, DozerBeanMapper mapper, 
                             EntityManager entityManager) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
    }
    
    /**
     * Obtiene el carrito de compra del usuario autenticado.
     *
     * @return Optional que contiene el carrito de compra si existe, vacío en caso contrario
     */
    @Override
    public Optional<CarritoCompra> getCarrito() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioPL usuarioPL = (UsuarioPL) authentication.getPrincipal();

        return this.carritoRepository.findByUsuarioId(usuarioPL.getId())
            .map(carrito -> mapper.map(carrito, CarritoCompra.class));
    }
    
    /**
     * Añade un producto al carrito con la cantidad especificada.
     *
     * @param idProducto ID del producto a añadir
     * @param cantidad Cantidad del producto a añadir
     * @throws EntityNotFoundException Si no se encuentra el carrito o el producto
     * @throws IllegalStateException Si el producto ya está en el carrito o no hay suficiente stock
     */
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
    
    /**
     * Modifica la cantidad de un producto en el carrito.
     * Si la nueva cantidad es 0 o menor, elimina el producto del carrito.
     *
     * @param idProducto ID del producto a modificar
     * @param nuevaCantidad Nueva cantidad del producto
     * @return Optional con el carrito actualizado
     * @throws EntityNotFoundException Si no se encuentra el carrito, producto o item
     * @throws IllegalStateException Si no hay suficiente stock disponible
     */
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

    /**
     * Elimina un producto del carrito.
     *
     * @param idProducto ID del producto a eliminar
     * @return Optional con el carrito actualizado
     * @throws EntityNotFoundException Si no se encuentra el carrito o el producto no está en el carrito
     */
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

    /**
     * Elimina todos los productos del carrito.
     *
     * @return Optional con el carrito vacío
     * @throws EntityNotFoundException Si no se encuentra el carrito
     */
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