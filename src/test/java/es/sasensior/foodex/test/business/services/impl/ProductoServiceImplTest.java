package es.sasensior.foodex.test.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.model.ImagenOrigen;
import es.sasensior.foodex.business.model.Producto;
import es.sasensior.foodex.business.services.impl.ProductoServiceImpl;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private CategoriaRepository categoriaRepository;
    
    @Mock
    private DozerBeanMapper mapper;
    
    @InjectMocks
    private ProductoServiceImpl productoService;
    
    private ProductoPL productoPL;
    private Producto producto;
    private CategoriaPL categoriaPL;
    private final Long PRODUCTO_ID = 1L;
    private final Long CATEGORIA_ID = 1L;
    private final String PRODUCTO_NOMBRE = "Test Producto";
    private final Double PRODUCTO_PRECIO = 10.99;
    private final Integer PRODUCTO_STOCK = 100;
    
    @BeforeEach
    void setUp() {
        categoriaPL = new CategoriaPL();
        categoriaPL.setId(CATEGORIA_ID);
        
        productoPL = new ProductoPL();
        productoPL.setId(PRODUCTO_ID);
        productoPL.setNombre(PRODUCTO_NOMBRE);
        productoPL.setPrecio(PRODUCTO_PRECIO);
        productoPL.setStock(PRODUCTO_STOCK);
        productoPL.setDescatalogado(false);
        productoPL.setCategoria(categoriaPL);
        productoPL.setFechaAlta(new Date());
        
        producto = new Producto();
        producto.setId(PRODUCTO_ID);
        producto.setNombre(PRODUCTO_NOMBRE);
        producto.setPrecio(PRODUCTO_PRECIO);
        producto.setStock(PRODUCTO_STOCK);
        producto.setDescatalogado(false);
        
        Categoria categoria = new Categoria();
        categoria.setId(CATEGORIA_ID);
        producto.setCategoria(categoria);
    }
    
    @Test
    void getAll_WhenProductsExist_ReturnsList() {
        List<ProductoPL> productoPLList = List.of(productoPL);
        when(productoRepository.findAll()).thenReturn(productoPLList);
        when(mapper.map(productoPL, Producto.class)).thenReturn(producto);
        
        List<Producto> result = productoService.getAll();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(PRODUCTO_NOMBRE, result.get(0).getNombre());
        verify(productoRepository).findAll();
        verify(mapper).map(productoPL, Producto.class);
    }

    @Test
    void getAll_WhenNoProducts_ReturnsEmptyList() {
        when(productoRepository.findAll()).thenReturn(List.of());
        
        List<Producto> result = productoService.getAll();
        
        assertTrue(result.isEmpty());
        verify(productoRepository).findAll();
        verify(mapper, never()).map(any(), any());
    }
    
    @Test
    void getProductosByCategoria_WhenValidCategory_ReturnsProducts() {
        List<ProductoPL> productoPLList = List.of(productoPL);
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.of(categoriaPL));
        when(productoRepository.findByCategoriaId(CATEGORIA_ID)).thenReturn(productoPLList);
        when(mapper.map(productoPL, Producto.class)).thenReturn(producto);
        
        List<Producto> result = productoService.getProductosByCategoria(CATEGORIA_ID);
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(categoriaRepository).findById(CATEGORIA_ID);
        verify(productoRepository).findByCategoriaId(CATEGORIA_ID);
    }

    @Test
    void getProductosByCategoria_WhenCategoryNotFound_ThrowsException() {
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            productoService.getProductosByCategoria(CATEGORIA_ID));
        verify(productoRepository, never()).findByCategoriaId(any());
    }

    @Test
    void getProductosByCategoria_WhenNoProducts_ReturnsEmptyList() {
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.of(categoriaPL));
        when(productoRepository.findByCategoriaId(CATEGORIA_ID)).thenReturn(List.of());
        
        List<Producto> result = productoService.getProductosByCategoria(CATEGORIA_ID);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void getProducto_WhenExists_ReturnsProducto() {
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(productoPL));
        when(mapper.map(productoPL, Producto.class)).thenReturn(producto);
        
        Optional<Producto> result = productoService.getProducto(PRODUCTO_ID);
        
        assertTrue(result.isPresent());
        assertEquals(PRODUCTO_ID, result.get().getId());
        verify(productoRepository).findById(PRODUCTO_ID);
    }

    @Test
    void getProducto_WhenNotExists_ThrowsException() {
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            productoService.getProducto(PRODUCTO_ID));
    }
    
    @Test
    void createProducto_WhenValidData_CreatesProducto() {
        Producto newProducto = new Producto();
        newProducto.setNombre("Nuevo Producto");
        newProducto.setPrecio(9.99);
        newProducto.setStock(50);
        newProducto.setDescatalogado(false);
        
        Categoria categoria = new Categoria();
        categoria.setId(CATEGORIA_ID);
        newProducto.setCategoria(categoria);
        
        ProductoPL newProductoPL = new ProductoPL();
        newProductoPL.setNombre("Nuevo Producto");
        newProductoPL.setPrecio(9.99);
        newProductoPL.setStock(50);
        newProductoPL.setDescatalogado(false);
        newProductoPL.setCategoria(categoriaPL);
        
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.of(categoriaPL));
        when(mapper.map(newProducto, ProductoPL.class)).thenReturn(newProductoPL);
        when(productoRepository.save(newProductoPL)).thenReturn(productoPL);
        
        productoService.createProducto(newProducto);
        
        verify(productoRepository).save(argThat(p -> 
            p.getNombre().equals("Nuevo Producto") && 
            p.getFechaAlta() != null));
    }

    @Test
    void createProducto_WhenIdNotNull_ThrowsException() {
        producto.setId(PRODUCTO_ID);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void createProducto_WhenNombreNull_ThrowsException() {
        producto.setNombre(null);
        
        assertThrows(IllegalArgumentException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenPrecioNull_ThrowsException() {
        producto.setPrecio(null);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenStockNull_ThrowsException() {
        producto.setStock(null);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenDescatalogadoNull_ThrowsException() {
        producto.setDescatalogado(null);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenCategoriaNull_ThrowsException() {
        producto.setCategoria(null);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenCategoriaIdNull_ThrowsException() {
        producto.getCategoria().setId(null);
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenFechaAltaNotNull_ThrowsException() {
        producto.setFechaAlta(new Date());
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenCategoriaDetailsProvided_ThrowsException() {
        producto.getCategoria().setNombre("Test");
        producto.getCategoria().setDescripcion("Desc");
        producto.getCategoria().setImgUrl("img.jpg");
        
        assertThrows(IllegalStateException.class, () -> 
            productoService.createProducto(producto));
    }

    @Test
    void createProducto_WhenCategoriaNotFound_ThrowsException() {
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            productoService.createProducto(producto));
    }
    
    @Test
    void updateProducto_WhenValidData_UpdatesProducto() {
        Producto updatedProducto = new Producto();
        updatedProducto.setId(PRODUCTO_ID);
        updatedProducto.setNombre("Updated Product");
        updatedProducto.setPrecio(12.99);
        updatedProducto.setStock(75);
        updatedProducto.setDescatalogado(true);
        updatedProducto.setImgUrl("new-image.jpg");
        updatedProducto.setImgOrigen(ImagenOrigen.UPLOAD);
        
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(productoPL));
        when(mapper.map(productoPL, Producto.class)).thenReturn(updatedProducto);
        when(productoRepository.save(productoPL)).thenReturn(productoPL);
        
        Producto result = productoService.updateProducto(updatedProducto);
        
        assertNotNull(result);
        verify(productoPL).setNombre("Updated Product");
        verify(productoPL).setPrecio(12.99);
        verify(productoPL).setStock(75);
        verify(productoPL).setDescatalogado(true);
        verify(productoPL).setImgUrl("new-image.jpg");
        verify(productoPL).setImgOrigen(ImagenOrigen.UPLOAD);
        verify(productoRepository).save(productoPL);
    }

    @Test
    void updateProducto_WhenNotExists_ThrowsException() {
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            productoService.updateProducto(producto));
    }

    @Test
    void updateProducto_WhenNombreNull_ThrowsException() {
        producto.setNombre(null);
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(productoPL));
        
        assertThrows(IllegalArgumentException.class, () -> 
            productoService.updateProducto(producto));
    }

    @Test
    void updateProducto_WhenNombreEmpty_ThrowsException() {
        producto.setNombre("");
        when(productoRepository.findById(PRODUCTO_ID)).thenReturn(Optional.of(productoPL));
        
        assertThrows(IllegalArgumentException.class, () -> 
            productoService.updateProducto(producto));
    }
    
}