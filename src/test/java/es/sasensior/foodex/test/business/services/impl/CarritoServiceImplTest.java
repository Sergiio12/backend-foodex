package es.sasensior.foodex.test.business.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.sasensior.foodex.business.model.CarritoCompra;
import es.sasensior.foodex.business.services.impl.CarritoServiceImpl;
import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.integration.dao.ItemCarritoPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CarritoRepository;
import es.sasensior.foodex.integration.repositories.ItemCarritoRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;
    
    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private ItemCarritoRepository itemCarritoRepository;
    
    @Mock
    private DozerBeanMapper mapper;
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private CarritoServiceImpl carritoService;
    
    private UsuarioPL usuarioPL;
    private CarritoCompraPL carritoPL;
    private ProductoPL productoPL;
    private ItemCarritoPL itemCarritoPL;
    private CarritoCompra carritoCompra;
    
    @BeforeEach
    void setUp() {
        usuarioPL = new UsuarioPL();
        usuarioPL.setId(1L);
        
        carritoPL = new CarritoCompraPL();
        carritoPL.setId(1L);
        carritoPL.setUsuario(usuarioPL);
        
        productoPL = new ProductoPL();
        productoPL.setId(1L);
        productoPL.setStock(10);
        
        itemCarritoPL = new ItemCarritoPL();
        itemCarritoPL.setCarrito(carritoPL);
        itemCarritoPL.setProducto(productoPL);
        itemCarritoPL.setCantidad(2);
        
        carritoCompra = new CarritoCompra();
        carritoCompra.setId(1L);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(usuarioPL);
    }
    
    @Test
    void getCarrito_WhenCarritoExists_ReturnsCarrito() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.getCarrito();
        
        assertTrue(result.isPresent());
        assertEquals(carritoCompra, result.get());
        verify(carritoRepository).findByUsuarioId(usuarioPL.getId());
        verify(mapper).map(carritoPL, CarritoCompra.class);
    }

    @Test
    void getCarrito_WhenCarritoNotExists_ReturnsEmpty() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.empty());
        
        Optional<CarritoCompra> result = carritoService.getCarrito();
        
        assertTrue(result.isEmpty());
        verify(carritoRepository).findByUsuarioId(usuarioPL.getId());
        verify(mapper, never()).map(any(), any());
    }
    
    @Test
    void addProductoToCarrito_WhenValidData_AddsProductToCarrito() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.empty());
        
        carritoService.addProductoToCarrito(1L, 3);
        
        verify(itemCarritoRepository).save(any(ItemCarritoPL.class));
    }

    @Test
    void addProductoToCarrito_WhenCarritoNotFound_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            carritoService.addProductoToCarrito(1L, 1));
        verify(productoRepository, never()).findById(any());
    }

    @Test
    void addProductoToCarrito_WhenProductoNotFound_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            carritoService.addProductoToCarrito(1L, 1));
    }

    @Test
    void addProductoToCarrito_WhenProductAlreadyInCarrito_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.of(itemCarritoPL));
        
        assertThrows(IllegalStateException.class, () -> 
            carritoService.addProductoToCarrito(1L, 1));
        verify(itemCarritoRepository, never()).save(any());
    }

    @Test
    void addProductoToCarrito_WhenNotEnoughStock_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.empty());
        
        productoPL.setStock(2);
        
        assertThrows(IllegalStateException.class, () -> 
            carritoService.addProductoToCarrito(1L, 3));
        verify(itemCarritoRepository, never()).save(any());
    }
    
    @Test
    void modifyProductoInCarrito_WhenValidData_UpdatesQuantity() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.of(itemCarritoPL));
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.modifyProductoInCarrito(1L, 5);
        
        assertTrue(result.isPresent());
        assertEquals(5, itemCarritoPL.getCantidad());
        verify(itemCarritoRepository).save(itemCarritoPL);
    }

    @Test
    void modifyProductoInCarrito_WhenNewQuantityIsZero_RemovesProduct() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.of(itemCarritoPL));
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.modifyProductoInCarrito(1L, 0);
        
        assertTrue(result.isPresent());
        verify(itemCarritoRepository).delete(itemCarritoPL);
    }

    @Test
    void modifyProductoInCarrito_WhenNotEnoughStock_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.of(itemCarritoPL));
        
        productoPL.setStock(2);
        
        assertThrows(IllegalStateException.class, () -> 
            carritoService.modifyProductoInCarrito(1L, 3));
        verify(itemCarritoRepository, never()).save(any());
    }

    @Test
    void modifyProductoInCarrito_WhenProductNotInCarrito_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPL));
        when(itemCarritoRepository.findByCarritoIdAndProductoId(carritoPL.getId(), productoPL.getId()))
            .thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            carritoService.modifyProductoInCarrito(1L, 2));
        verify(itemCarritoRepository, never()).save(any());
    }
    
    @Test
    void removeProductoFromCarrito_WhenValidData_RemovesProduct() {
        carritoPL.getItemsCarrito().add(itemCarritoPL);
        
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.removeProductoFromCarrito(1L);
        
        assertTrue(result.isPresent());
        verify(itemCarritoRepository).delete(itemCarritoPL);
        verify(carritoRepository).flush();
        verify(entityManager).refresh(carritoPL);
    }

    @Test
    void removeProductoFromCarrito_WhenProductNotInCarrito_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        
        assertThrows(EntityNotFoundException.class, () -> 
            carritoService.removeProductoFromCarrito(1L));
        verify(itemCarritoRepository, never()).delete(any());
    }
    
    @Test
    void removeAllProductosFromCarrito_WhenValidData_RemovesAllProducts() {
        carritoPL.getItemsCarrito().add(itemCarritoPL);
        
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.removeAllProductosFromCarrito();
        
        assertTrue(result.isPresent());
        verify(itemCarritoRepository).deleteAllByCarrito(carritoPL.getId());
    }

    @Test
    void removeAllProductosFromCarrito_WhenCarritoEmpty_ReturnsEmptyCarrito() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.of(carritoPL));
        when(mapper.map(carritoPL, CarritoCompra.class)).thenReturn(carritoCompra);
        
        Optional<CarritoCompra> result = carritoService.removeAllProductosFromCarrito();
        
        assertTrue(result.isPresent());
        verify(itemCarritoRepository).deleteAllByCarrito(carritoPL.getId());
    }

    @Test
    void removeAllProductosFromCarrito_WhenCarritoNotFound_ThrowsException() {
        when(carritoRepository.findByUsuarioId(usuarioPL.getId())).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            carritoService.removeAllProductosFromCarrito());
        verify(itemCarritoRepository, never()).deleteAllByCarrito(any());
    }
    
}