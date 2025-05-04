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

@RequiredArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DozerBeanMapper mapper;

    @Override
    public List<Producto> getAll() {
        List<ProductoPL> productosPL = productoRepository.findAll();
        return convertProductosPLToProductos(productosPL);
    }
    
    @Override
    public List<Producto> getProductosByCategoria(Long idCategoria) {
        categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new EntityNotFoundException(
                "No se ha encontrado la categoría con id " + idCategoria));

        List<ProductoPL> productosPL = productoRepository.findByCategoriaId(idCategoria);
        return convertProductosPLToProductos(productosPL);
    }

    
    @Override
    public Optional<Producto> getProducto(Long idProducto) {
        ProductoPL productoPL = productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado ningún producto con ese id."));
        return Optional.of(mapper.map(productoPL, Producto.class));
    }

    @Override
    @Transactional
    public void createProducto(Producto producto) {
        
        if (producto.getId() != null) {
            throw new IllegalStateException("El id del producto a crear debe ser nulo.");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo ni estar vacío.");
        }

        if (producto.getPrecio() == null) {
            throw new IllegalStateException("El precio del producto no puede ser nulo.");
        }

        if (producto.getStock() == null) {
            throw new IllegalStateException("El stock del producto no puede ser nulo.");
        }

        if (producto.getDescatalogado() == null) {
            throw new IllegalStateException("El estado de descatalogado no puede ser nulo.");
        }

        if (producto.getCategoria() == null) {
            throw new IllegalStateException("La categoría no puede ser nula.");
        }

        if (producto.getCategoria().getId() == null) {
            throw new IllegalStateException("Debes especificar el id de una categoría existente.");
        }
        
        if (producto.getFechaAlta() != null) {
            throw new IllegalStateException("No puedes manipular manualmente el campo de fecha de alta. Déjalo nulo.");
        }

        // No se debe modificar la categoría en la creación
        if (producto.getCategoria().getNombre() != null || producto.getCategoria().getDescripcion() != null || producto.getCategoria().getImgUrl() != null) {
            throw new IllegalStateException("No especifiques el nombre, la descripción o la imagen de la categoría. Solo indica el id.");
        }
        
        CategoriaPL categoriaPL = this.categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado la categoría que buscas."));
        
        ProductoPL productoPL = mapper.map(producto, ProductoPL.class);
        productoPL.setCategoria(categoriaPL);
        productoPL.setFechaAlta(new Date());
        
        // Guardar el nuevo producto
        this.productoRepository.save(productoPL);
    }

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

    // ********************************************
    //
    // Métodos Privados
    //
    // ********************************************

    private List<Producto> convertProductosPLToProductos(List<ProductoPL> productoPLList) {
        return productoPLList.stream()
                .map(x -> mapper.map(x, Producto.class))
                .toList();
    }
}
