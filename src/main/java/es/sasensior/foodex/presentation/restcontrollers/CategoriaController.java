package es.sasensior.foodex.presentation.restcontrollers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.services.CategoriaService;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(path = "api/categorias")
public class CategoriaController {
	
	public final CategoriaService categoriaService;
	
	public CategoriaController(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}
	
	@GetMapping
	public ResponseEntity<ApiResponseBody> getAll() {
		return ResponseEntity.ok(new ApiResponseBody.Builder("Categorías obtenidas con éxito")
			.status(ResponseStatus.SUCCESS)
			.data(this.categoriaService.getAll())
			.build());
	}
	
	@GetMapping(params = "id")
	public ResponseEntity<?> getCategoria(@RequestParam("id") Long idCategoria) {
		Optional<Categoria> categoria;
		try {
			categoria = this.categoriaService.getCategoria(idCategoria);
		} catch(EntityNotFoundException e) {
			throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage()).build();
		}
	
		return ResponseEntity.ok(new ApiResponseBody.Builder("Categoría obtenida con éxito.")
			.status(ResponseStatus.SUCCESS)
			.data(categoria.get())
			.build());
		
	}
	
	@PostMapping
	public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
		try {
			this.categoriaService.createCategoria(categoria);
		} catch(IllegalArgumentException | IllegalStateException e) {
			throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
				.build();
		}
		
		return ResponseEntity.ok(new ApiResponseBody.Builder("Categoría creada con éxito.")
			.status(ResponseStatus.SUCCESS)
			.build());
		
	}
	
	@PutMapping
	public ResponseEntity<?> updateCategoria(@RequestBody Categoria categoria) {
		try {
			this.categoriaService.updateCategoria(categoria);
		} catch(IllegalArgumentException e) {
			throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
				.build();
		}
		
		return ResponseEntity.ok(new ApiResponseBody.Builder("Categoría actualizada con éxito.")
			.status(ResponseStatus.SUCCESS)
			.build());
		
	}
	

}
