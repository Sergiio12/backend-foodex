package es.sasensior.foodex.presentation.restcontrollers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.sasensior.foodex.business.model.Categoria;
import es.sasensior.foodex.business.model.ImagenOrigen;
import es.sasensior.foodex.business.services.CategoriaService;
import es.sasensior.foodex.business.services.ImageService;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import es.sasensior.foodex.security.payloads.CategoriaUpdateRequest;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(path = "api/categorias")
public class CategoriaController {
	
	private final CategoriaService categoriaService;
	private final ImageService imageService;
	
	public CategoriaController(CategoriaService categoriaService, ImageService imageService) {
		this.categoriaService = categoriaService;
		this.imageService = imageService;
	}
	
	@GetMapping
	public ResponseEntity<ApiResponseBody> getAll() {
		return ResponseEntity.ok(new ApiResponseBody.Builder("Categorías obtenidas con éxito")
			.status(ResponseStatus.SUCCESS)
			.data(this.categoriaService.getAll())
			.build());
	}
	
	@GetMapping(params = "id")
	public ResponseEntity<ApiResponseBody> getCategoria(@RequestParam("id") Long idCategoria) {
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
	public ResponseEntity<ApiResponseBody> createCategoria(@RequestBody Categoria categoria) {
	    Categoria createdCategoria;
	    try {
	        createdCategoria = this.categoriaService.createCategoria(categoria); 
	    } catch(IllegalArgumentException | IllegalStateException e) {
	        throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage()).build();
	    }
	    return ResponseEntity.ok(new ApiResponseBody.Builder("Categoría creada con éxito.")
	        .status(ResponseStatus.SUCCESS)
	        .data(createdCategoria) 
	        .build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseBody> deleteCategoria(@PathVariable Long id) {
	    try {
	        categoriaService.deleteCategoria(id);
	        return ResponseEntity.ok(
	            new ApiResponseBody.Builder("Categoría eliminada exitosamente")
	                .status(ResponseStatus.SUCCESS)
	                .build()
	        );
	    } catch (EntityNotFoundException e) {
	        throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
	    } catch (Exception e) {
	        throw new PresentationException.Builder(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la categoría").build();
	    }
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseBody> updateCategoria(@PathVariable Long id, 
			@RequestBody CategoriaUpdateRequest request) {
	    
	    Optional<Categoria> optionalCategoria = categoriaService.getCategoria(id);
	    if (!optionalCategoria.isPresent()) {
	    	throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, "No se ha encontrado la categoría que buscas.").build();
	    }
	    Categoria categoria = optionalCategoria.get();
	    categoria.setNombre(request.getNombre());
	    categoria.setDescripcion(request.getDescripcion());
	    
	    Categoria updatedCategoria = categoriaService.updateCategoria(categoria);
	    
	    return ResponseEntity.ok(new ApiResponseBody.Builder("Categoría actualizada exitosamente.")
				.status(ResponseStatus.SUCCESS)
                .data(updatedCategoria)
				.build());
	}
	
	@PostMapping("/{id}/upload-image")
	public ResponseEntity<ApiResponseBody> uploadImage(@PathVariable Long id, 
			@RequestParam MultipartFile file) {
	    try {
	        Optional<Categoria> optionalCategoria = categoriaService.getCategoria(id);
	        if (!optionalCategoria.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponseBody.Builder("No se ha encontrado la categoría que buscas.")
	                        .status(ResponseStatus.ERROR)
	                        .build());
	        }
	        Categoria categoria = optionalCategoria.get();
	        
	        String imageUrl = imageService.persistImage(file);
	        
	        categoria.setImgUrl(imageUrl);
	        categoria.setImgOrigen(ImagenOrigen.UPLOAD);
	        
	        Categoria updatedCategoria = categoriaService.updateCategoria(categoria);
	        
	        return ResponseEntity.ok(new ApiResponseBody.Builder("Imagen de categoría subida y asociada correctamente.")
	            .status(ResponseStatus.SUCCESS)
	            .data(updatedCategoria)
	            .build());
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponseBody.Builder("Error al subir la imagen: " + e.getMessage())
	                        .status(ResponseStatus.ERROR)
	                        .build());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest()
	                .body(new ApiResponseBody.Builder(e.getMessage())
	                        .status(ResponseStatus.ERROR)
	                        .build());
	    }
	}
}
