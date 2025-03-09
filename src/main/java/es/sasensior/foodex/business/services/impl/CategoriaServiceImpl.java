package es.sasensior.foodex.business.services.impl;

import java.util.List;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import es.sasensior.foodex.business.model.Categoria;
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
	private DozerBeanMapper mapper;
	
	public CategoriaServiceImpl(CategoriaRepository categoriaRepository, DozerBeanMapper mapper) {
		this.categoriaRepository = categoriaRepository;
		this.mapper = mapper;
	}

	@Override
	public List<Categoria> getAll() {
		return this.convertClientesPLToClientes(this.categoriaRepository.findAll());
	}

	@Override
	public Optional<Categoria> getCategoria(Long idCategoria) {
	    Optional<CategoriaPL> categoriaPL = this.categoriaRepository.findById(idCategoria);
	    
	    return Optional.of(categoriaPL.map(categoria -> mapper.map(categoria, Categoria.class))
	                      .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado ninguna categoría con ese id.")));
	}


	@Override
	@Transactional
	public void createCategoria(Categoria categoria) {
		
		if(categoria.getId() != null) {
			throw new IllegalArgumentException("El id de la categoría a crear ha de ser nulo.");
		}
		
		if(this.categoriaRepository.existsByNombre(categoria.getNombre())) {
			throw new IllegalStateException("Ya existe una categoría con ese mismo nombre.");
		}
		
		this.categoriaRepository.save(mapper.map(categoria, CategoriaPL.class));
		
	}

	@Override
	@Transactional
	public void updateCategoria(Categoria categoria) {
		
		if(categoria.getId() == null) {
			throw new IllegalArgumentException("Debes especificar un id para actualizar la categoría");
		}
		
		this.categoriaRepository.save(mapper.map(categoria, CategoriaPL.class));
		
	}
	
	// ********************************************
	//
	// Private Methods
	//
	// ********************************************
		
		private List<Categoria> convertClientesPLToClientes(List<CategoriaPL> categoriaPL) {
			return categoriaPL.stream()
					.map(x -> mapper.map(x, Categoria.class))
					.toList();
		}

}
