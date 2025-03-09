package es.sasensior.foodex.business.services;

import java.util.List;
import java.util.Optional;

import es.sasensior.foodex.business.model.Categoria;

public interface CategoriaService {
	
	List<Categoria> getAll();
	
	Optional<Categoria> getCategoria(Long idCategoria);
	
	void createCategoria(Categoria categoria);
	
	void updateCategoria(Categoria categoria);

}
