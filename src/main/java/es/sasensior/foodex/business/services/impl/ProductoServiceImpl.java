package es.sasensior.foodex.business.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Producto;
import es.sasensior.foodex.business.services.ProductoService;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio para la gestión de productos.
 * Proporciona funcionalidades CRUD para productos y métodos para buscar productos por categoría.
 * 
 * @Service Indica que esta clase es un componente de servicio de Spring.
 * @RequiredArgsConstructor Genera un constructor con los campos finales para inyección de dependencias
 */
@RequiredArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DozerBeanMapper mapper;

    /**
     * Obtiene todos los productos existentes.
     *
     * @return Lista de todos los productos
     */
    @Override
    public List<Producto> getAll() {
        List<ProductoPL> productosPL = productoRepository.findAll();
        return convertProductosPLToProductos(productosPL);
    }
    
    /**
     * Obtiene los productos pertenecientes a una categoría específica.
     *
     * @param idCategoria ID de la categoría para filtrar productos
     * @return Lista de productos de la categoría especificada
     * @throws EntityNotFoundException Si no se encuentra la categoría con el ID especificado
     */
    @Override
    public List<Producto> getProductosByCategoria(Long idCategoria) {
        categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se ha encontrado la categoría con id " + idCategoria));

        List<ProductoPL> productosPL = productoRepository.findByCategoriaId(idCategoria);
        return convertProductosPLToProductos(productosPL);
    }

    /**
     * Obtiene un producto específico por su ID.
     *
     * @param idProducto ID del producto a buscar
     * @return Optional que contiene el producto si existe
     * @throws EntityNotFoundException Si no se encuentra ningún producto con el ID especificado
     */
    @Override
    public Optional<Producto> getProducto(Long idProducto) {
        ProductoPL productoPL = productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado ningún producto con ese id."));
        return Optional.of(mapper.map(productoPL, Producto.class));
    }

    /**
     * Crea un nuevo producto.
     *
     * @param producto Producto a crear
     * @throws IllegalStateException Si:
     *         - El ID del producto no es nulo
     *         - El precio, stock o estado descatalogado son nulos
     *         - La categoría es nula o no tiene ID
     *         - Se intenta manipular manualmente la fecha de alta
     *         - Se especifican detalles de categoría además del ID
     * @throws IllegalArgumentException Si el nombre del producto es nulo o vacío
     * @throws EntityNotFoundException Si no se encuentra la categoría especificada
     */
    @Override
    @Transactional
    public Producto createProducto(Producto producto) {
        
        if (producto.getId() != null) {
            throw new IllegalStateException("El id del producto a crear debe ser nulo.");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo ni estar vacío.");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalStateException("El precio debe ser un valor positivo.");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalStateException("El stock no puede ser negativo.");
        }

        if (producto.getDescatalogado() == null) {
            throw new IllegalStateException("El estado de descatalogado no puede ser nulo.");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalStateException("Debes especificar el id de una categoría existente.");
        }

        if (producto.getFechaAlta() != null) {
            throw new IllegalStateException("No puedes manipular manualmente el campo de fecha de alta.");
        }

        if (producto.getCategoria().getNombre() != null 
            || producto.getCategoria().getDescripcion() != null 
            || producto.getCategoria().getImgUrl() != null) {
            throw new IllegalStateException("Solo se debe proporcionar el ID de la categoría.");
        }

        CategoriaPL categoriaPL = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + producto.getCategoria().getId()));

        ProductoPL productoPL = mapper.map(producto, ProductoPL.class);
        productoPL.setCategoria(categoriaPL);
        productoPL.setFechaAlta(new Date());
        
        ProductoPL savedProductoPL = productoRepository.save(productoPL);
        
        return mapper.map(savedProductoPL, Producto.class);
    }
    
    /**
     * Elimina un producto existente por su ID. 
     * Al eliminar el producto, se borrarán automáticamente todas sus referencias en:
     * - Tabla PRODUCTOS_COMPRAS (histórico de compras)
     * - Tabla ITEMS_CARRITO (carritos de usuarios)
     * 
     * @param idProducto ID del producto a eliminar
     * @throws EntityNotFoundException Si no se encuentra ningún producto con el ID especificado
     */
    @Override
    @Transactional
    public void deleteProducto(Long idProducto) {
        ProductoPL productoPL = productoRepository.findById(idProducto)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + idProducto));
        
        productoRepository.delete(productoPL);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto Producto con los datos actualizados
     * @return El producto actualizado
     * @throws EntityNotFoundException Si no se encuentra el producto a actualizar
     * @throws IllegalArgumentException Si el nombre es nulo o vacío
     */
    @Override
    @Transactional
    public Producto updateProducto(Producto producto) {
        ProductoPL productoExistente = productoRepository.findById(producto.getId())
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es requerido");
        }

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setDescatalogado(producto.getDescatalogado());
        
        productoExistente.setImgUrl(producto.getImgUrl());
        productoExistente.setImgOrigen(producto.getImgOrigen());

        return mapper.map(productoRepository.save(productoExistente), Producto.class);
    }

    /**
     * Convierte una lista de entidades ProductoPL a modelos Producto.
     *
     * @param productoPLList Lista de entidades ProductoPL
     * @return Lista de modelos Producto
     */
    private List<Producto> convertProductosPLToProductos(List<ProductoPL> productoPLList) {
        return productoPLList.stream()
                .map(x -> mapper.map(x, Producto.class))
                .toList();
    }
}