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
import es.sasensior.foodex.business.services.impl.CategoriaServiceImpl;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;
    
    @Mock
    private DozerBeanMapper mapper;
    
    @InjectMocks
    private CategoriaServiceImpl categoriaService;
    
    private CategoriaPL categoriaPL;
    private Categoria categoria;
    private final Long CATEGORIA_ID = 1L;
    private final String CATEGORIA_NOMBRE = "Test Categoria";
    
    @BeforeEach
    void setUp() {
        categoriaPL = new CategoriaPL();
        categoriaPL.setId(CATEGORIA_ID);
        categoriaPL.setNombre(CATEGORIA_NOMBRE);
        categoriaPL.setImgOrigen(ImagenOrigen.STATIC);
        
        categoria = new Categoria();
        categoria.setId(CATEGORIA_ID);
        categoria.setNombre(CATEGORIA_NOMBRE);
        categoria.setImgOrigen(ImagenOrigen.STATIC);
    }
    
    @Test
    void getAll_WhenCategoriesExist_ReturnsList() {
        List<CategoriaPL> categoriaPLList = List.of(categoriaPL);
        when(categoriaRepository.findAll()).thenReturn(categoriaPLList);
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        List<Categoria> result = categoriaService.getAll();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(CATEGORIA_NOMBRE, result.get(0).getNombre());
        verify(categoriaRepository).findAll();
        verify(mapper).map(categoriaPL, Categoria.class);
    }

    @Test
    void getAll_WhenNoCategories_ReturnsEmptyList() {
        when(categoriaRepository.findAll()).thenReturn(List.of());
        
        List<Categoria> result = categoriaService.getAll();
        
        assertTrue(result.isEmpty());
        verify(categoriaRepository).findAll();
        verify(mapper, never()).map(any(), any());
    }
    
    @Test
    void getCategoria_WhenExists_ReturnsCategoria() {
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.of(categoriaPL));
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        Optional<Categoria> result = categoriaService.getCategoria(CATEGORIA_ID);
        
        assertTrue(result.isPresent());
        assertEquals(CATEGORIA_ID, result.get().getId());
        verify(categoriaRepository).findById(CATEGORIA_ID);
        verify(mapper).map(categoriaPL, Categoria.class);
    }

    @Test
    void getCategoria_WhenNotExists_ThrowsEntityNotFoundException() {
        when(categoriaRepository.findById(CATEGORIA_ID)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            categoriaService.getCategoria(CATEGORIA_ID));
        verify(categoriaRepository).findById(CATEGORIA_ID);
        verify(mapper, never()).map(any(), any());
    }
    
    @Test
    void createCategoria_WhenValidData_CreatesCategoria() {
        Categoria newCategoria = new Categoria();
        newCategoria.setNombre("Nueva Categoria");
        
        CategoriaPL newCategoriaPL = new CategoriaPL();
        newCategoriaPL.setNombre("Nueva Categoria");
        
        when(categoriaRepository.existsByNombre(newCategoria.getNombre())).thenReturn(false);
        when(mapper.map(newCategoria, CategoriaPL.class)).thenReturn(newCategoriaPL);
        when(categoriaRepository.save(newCategoriaPL)).thenReturn(categoriaPL);
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        Categoria result = categoriaService.createCategoria(newCategoria);
        
        assertNotNull(result);
        assertEquals(CATEGORIA_ID, result.getId());
        verify(categoriaRepository).existsByNombre(newCategoria.getNombre());
        verify(categoriaRepository).save(newCategoriaPL);
    }

    @Test
    void createCategoria_WhenIdNotNull_ThrowsIllegalArgumentException() {
        categoria.setId(CATEGORIA_ID);
        
        assertThrows(IllegalArgumentException.class, () -> 
            categoriaService.createCategoria(categoria));
        verify(categoriaRepository, never()).existsByNombre(any());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void createCategoria_WhenNameExists_ThrowsIllegalStateException() {
        Categoria newCategoria = new Categoria();
        newCategoria.setNombre(CATEGORIA_NOMBRE);
        
        when(categoriaRepository.existsByNombre(CATEGORIA_NOMBRE)).thenReturn(true);
        
        assertThrows(IllegalStateException.class, () -> 
            categoriaService.createCategoria(newCategoria));
        verify(categoriaRepository).existsByNombre(CATEGORIA_NOMBRE);
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void createCategoria_WhenImgOrigenNull_SetsDefaultValue() {
        Categoria newCategoria = new Categoria();
        newCategoria.setNombre("Nueva Categoria");
        newCategoria.setImgOrigen(null);
        
        CategoriaPL newCategoriaPL = new CategoriaPL();
        newCategoriaPL.setNombre("Nueva Categoria");
        newCategoriaPL.setImgOrigen(ImagenOrigen.STATIC);
        
        when(categoriaRepository.existsByNombre(newCategoria.getNombre())).thenReturn(false);
        when(mapper.map(newCategoria, CategoriaPL.class)).thenReturn(newCategoriaPL);
        when(categoriaRepository.save(newCategoriaPL)).thenReturn(categoriaPL);
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        Categoria result = categoriaService.createCategoria(newCategoria);
        
        assertNotNull(result);
        assertEquals(ImagenOrigen.STATIC, result.getImgOrigen());
        verify(categoriaRepository).save(argThat(c -> 
            c.getImgOrigen() == ImagenOrigen.STATIC));
    }
    
    @Test
    void updateCategoria_WhenValidData_UpdatesCategoria() {
        when(categoriaRepository.existsById(CATEGORIA_ID)).thenReturn(true);
        when(mapper.map(categoria, CategoriaPL.class)).thenReturn(categoriaPL);
        when(categoriaRepository.save(categoriaPL)).thenReturn(categoriaPL);
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        Categoria result = categoriaService.updateCategoria(categoria);
        
        assertNotNull(result);
        assertEquals(CATEGORIA_ID, result.getId());
        verify(categoriaRepository).existsById(CATEGORIA_ID);
        verify(categoriaRepository).save(categoriaPL);
    }

    @Test
    void updateCategoria_WhenIdNull_ThrowsIllegalArgumentException() {
        categoria.setId(null);
        
        assertThrows(IllegalArgumentException.class, () -> 
            categoriaService.updateCategoria(categoria));
        verify(categoriaRepository, never()).existsById(any());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void updateCategoria_WhenNotExists_ThrowsEntityNotFoundException() {
        when(categoriaRepository.existsById(CATEGORIA_ID)).thenReturn(false);
        
        assertThrows(EntityNotFoundException.class, () -> 
            categoriaService.updateCategoria(categoria));
        verify(categoriaRepository).existsById(CATEGORIA_ID);
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void updateCategoria_WhenImgOrigenNull_SetsDefaultValue() {
        categoria.setImgOrigen(null);
        
        when(categoriaRepository.existsById(CATEGORIA_ID)).thenReturn(true);
        when(mapper.map(categoria, CategoriaPL.class)).thenReturn(categoriaPL);
        when(categoriaRepository.save(categoriaPL)).thenReturn(categoriaPL);
        when(mapper.map(categoriaPL, Categoria.class)).thenReturn(categoria);
        
        Categoria result = categoriaService.updateCategoria(categoria);
        
        assertNotNull(result);
        assertEquals(ImagenOrigen.STATIC, result.getImgOrigen());
        verify(categoriaRepository).save(argThat(c -> 
            c.getImgOrigen() == ImagenOrigen.STATIC));
    }
    
}