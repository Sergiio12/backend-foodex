package es.sasensior.foodex.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.model.ImagenOrigen;
import es.sasensior.foodex.business.services.CategoriaService;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.dao.ProductoPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import es.sasensior.foodex.integration.repositories.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;

/**
 * Implementación del servicio para la gestión de categorías de productos.
 * Proporciona funcionalidades CRUD para las categorías de productos.
 * 
 * @Service Indica que esta clase es un componente de servicio de Spring.
 */
@Getter
@Service
public class CategoriaServiceImpl implements CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final DozerBeanMapper mapper;
    
    /**
     * Constructor para la inyección de dependencias.
     *
     * @param categoriaRepository Repositorio para operaciones con categorías
     * @param mapper Mapeador para conversión entre entidades y modelos
     */
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository, DozerBeanMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.mapper = mapper;
    }

    /**
     * Obtiene todas las categorías existentes.
     *
     * @return Lista de todas las categorías
     */
    @Override
    public List<Categoria> getAll() {
        return convertCategoriasPLToCategorias(this.categoriaRepository.findAll());
    }

    /**
     * Obtiene una categoría específica por su ID.
     *
     * @param idCategoria ID de la categoría a buscar
     * @return Optional que contiene la categoría si existe
     * @throws EntityNotFoundException Si no se encuentra ninguna categoría con el ID especificado
     */
    @Override
    public Optional<Categoria> getCategoria(Long idCategoria) {
        Optional<CategoriaPL> categoriaPL = this.categoriaRepository.findById(idCategoria);
        return categoriaPL.map(categoria -> mapper.map(categoria, Categoria.class))
                .or(() -> { throw new EntityNotFoundException("No se ha encontrado ninguna categoría con ese id."); });
    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoria Categoría a crear
     * @return La categoría creada
     * @throws IllegalArgumentException Si se intenta crear una categoría con ID no nulo
     * @throws IllegalStateException Si ya existe una categoría con el mismo nombre
     */
    @Override
    @Transactional
    public Categoria createCategoria(Categoria categoria) {
        if (categoria.getId() != null) {
            throw new IllegalArgumentException("El id de la categoría a crear ha de ser nulo.");
        }
        
        if (this.categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese mismo nombre.");
        }
        
        if (categoria.getImgOrigen() == null) {
            categoria.setImgOrigen(ImagenOrigen.STATIC);
        }
        
        CategoriaPL categoriaPL = mapper.map(categoria, CategoriaPL.class);
        CategoriaPL savedCategoriaPL = this.categoriaRepository.save(categoriaPL);
        return mapper.map(savedCategoriaPL, Categoria.class);
    }
    
    /**
     * Método para eliminar una categoría.
     * @param id El id de la categoría a eliminar.
     */
    @Override
    @Transactional
    public void deleteCategoria(Long id) {
        CategoriaPL categoriaPL = categoriaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        
        List<ProductoPL> productos = productoRepository.findByCategoriaId(id);
        productoRepository.deleteAll(productos);
        
        categoriaRepository.delete(categoriaPL); 
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param categoria Categoría con los datos actualizados
     * @return La categoría actualizada
     * @throws IllegalArgumentException Si no se especifica un ID para la categoría
     * @throws EntityNotFoundException Si no se encuentra la categoría a actualizar
     */
    @Override
    @Transactional
    public Categoria updateCategoria(Categoria categoria) {
        if (categoria.getId() == null) {
            throw new IllegalArgumentException("Debes especificar un id para actualizar la categoría");
        }
        
        if (!this.categoriaRepository.existsById(categoria.getId())) {
            throw new EntityNotFoundException("Categoría no encontrada para el id: " + categoria.getId());
        }
        
        if (categoria.getImgOrigen() == null) {
            categoria.setImgOrigen(ImagenOrigen.STATIC);
        }
        
        CategoriaPL categoriaPL = mapper.map(categoria, CategoriaPL.class);
        CategoriaPL updatedCategoriaPL = this.categoriaRepository.save(categoriaPL);
        return mapper.map(updatedCategoriaPL, Categoria.class);
    }
        
    /**
     * Convierte una lista de entidades CategoriaPL a modelos Categoria.
     *
     * @param categoriaPLList Lista de entidades CategoriaPL
     * @return Lista de modelos Categoria
     */
    private List<Categoria> convertCategoriasPLToCategorias(List<CategoriaPL> categoriaPLList) {
        return categoriaPLList.stream()
                .map(x -> mapper.map(x, Categoria.class))
                .toList();
    }
}