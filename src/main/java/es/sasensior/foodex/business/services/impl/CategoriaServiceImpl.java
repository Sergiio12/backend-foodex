package es.sasensior.foodex.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.model.ImagenOrigen;
import es.sasensior.foodex.business.services.CategoriaService;
import es.sasensior.foodex.integration.dao.CategoriaPL;
import es.sasensior.foodex.integration.repositories.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	private final CategoriaRepository categoriaRepository;
	private final DozerBeanMapper mapper;
	
	public CategoriaServiceImpl(CategoriaRepository categoriaRepository, DozerBeanMapper mapper) {
		this.categoriaRepository = categoriaRepository;
		this.mapper = mapper;
	}

	@Override
	public List<Categoria> getAll() {
		return convertCategoriasPLToCategorias(this.categoriaRepository.findAll());
	}

	@Override
	public Optional<Categoria> getCategoria(Long idCategoria) {
	    Optional<CategoriaPL> categoriaPL = this.categoriaRepository.findById(idCategoria);
	    return categoriaPL.map(categoria -> mapper.map(categoria, Categoria.class))
	            .or(() -> { throw new EntityNotFoundException("No se ha encontrado ninguna categoría con ese id."); });
	}

	@Override
	@Transactional
	public Categoria createCategoria(Categoria categoria) {
		if (categoria.getId() != null) {
			throw new IllegalArgumentException("El id de la categoría a crear ha de ser nulo.");
		}
		
		if (this.categoriaRepository.existsByNombre(categoria.getNombre())) {
			throw new IllegalStateException("Ya existe una categoría con ese mismo nombre.");
		}
		
		// Si no se especifica el origen de la imagen, se asume STATIC por defecto
		if (categoria.getImgOrigen() == null) {
			categoria.setImgOrigen(ImagenOrigen.STATIC);
		}
		
		CategoriaPL categoriaPL = mapper.map(categoria, CategoriaPL.class);
		CategoriaPL savedCategoriaPL = this.categoriaRepository.save(categoriaPL);
		return mapper.map(savedCategoriaPL, Categoria.class);
	}

	@Override
	@Transactional
	public Categoria updateCategoria(Categoria categoria) {
		if (categoria.getId() == null) {
			throw new IllegalArgumentException("Debes especificar un id para actualizar la categoría");
		}
		
		if (!this.categoriaRepository.existsById(categoria.getId())) {
			throw new EntityNotFoundException("Categoría no encontrada para el id: " + categoria.getId());
		}
		
		// Si no se especifica origen, aseguramos uno por defecto
		if (categoria.getImgOrigen() == null) {
			categoria.setImgOrigen(ImagenOrigen.STATIC);
		}
		
		CategoriaPL categoriaPL = mapper.map(categoria, CategoriaPL.class);
		CategoriaPL updatedCategoriaPL = this.categoriaRepository.save(categoriaPL);
		return mapper.map(updatedCategoriaPL, Categoria.class);
	}
	
	// ********************************************
	// Private Methods
	// ********************************************
		
	private List<Categoria> convertCategoriasPLToCategorias(List<CategoriaPL> categoriaPLList) {
		return categoriaPLList.stream()
				.map(x -> mapper.map(x, Categoria.class))
				.toList();
	}
}
