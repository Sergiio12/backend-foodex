package es.sasensior.foodex.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.model.Producto;
import es.sasensior.foodex.business.services.ProductoService;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Service
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private DozerBeanMapper mapper;
    
    public ProductoServiceImpl(ProductoRepository productoRepository, DozerBeanMapper mapper, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.mapper = mapper;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Producto> getAll() {
        return convertProductosPLToProductos(productoRepository.findAll());
    }

    @Override
    public Optional<Producto> getProducto(Long idProducto) {
        Optional<ProductoPL> productoPL = this.productoRepository.findById(idProducto);
        
        return Optional.of(productoPL.map(producto -> mapper.map(producto, Producto.class))
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado ningún producto con ese id.")));
        
    }

    @Override
    @Transactional
    public void createProducto(Producto producto) {
        if (producto.getId() != null) {
            throw new IllegalStateException("El id del objeto a crear debe ser nulo.");
        }
        
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            CategoriaPL categoriaExistente = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada."));
            
            producto.setCategoria(mapper.map(categoriaExistente, Categoria.class));
        }

        ProductoPL productoPL = mapper.map(producto, ProductoPL.class);
        productoRepository.save(productoPL);
    }

    @Override
    @Transactional
    public void updateProducto(Producto producto) {
        if (producto.getId() == null) {
            throw new IllegalArgumentException("Debe especificarse el id del producto en la solicitud para actualizarlo.");
        }
        
        ProductoPL productoExistente = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ningún producto con ese id."));

        // Validaciones de campos obligatorios
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo ni estar vacío.");
        }

        if (producto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio del producto no puede ser nulo.");
        }
        
        if (producto.getStock() == null) {
            throw new IllegalArgumentException("El stock del producto no puede ser nulo.");
        }
        
        if (producto.getDescatalogado() == null) {
            throw new IllegalArgumentException("El estado de descatalogado no puede ser nulo.");
        }

        if(producto.getCategoria() == null) {
        	producto.setCategoria(mapper.map(productoExistente.getCategoria(), Categoria.class));
        } else {
        	throw new IllegalStateException("La categoría no se puede modificar.");
        }
        
        producto.setFechaAlta(productoExistente.getFechaAlta()); //Mantenemos fecha de alta original.
        
        mapper.map(producto, productoExistente);

        // Guardar cambios
        productoRepository.save(productoExistente);
    }
    

    // ********************************************
    //
    // Private Methods
    //
    // ********************************************
    
    private List<Producto> convertProductosPLToProductos(List<ProductoPL> productoPLList) {
        return productoPLList.stream()
                .map(x -> mapper.map(x, Producto.class))
                .toList();
    }
}
