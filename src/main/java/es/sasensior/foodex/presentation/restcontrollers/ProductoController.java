package es.sasensior.foodex.presentation.restcontrollers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.sasensior.foodex.business.model.ImagenOrigen;
import es.sasensior.foodex.business.model.Producto;
import es.sasensior.foodex.business.services.ImageService;
import es.sasensior.foodex.business.services.ProductoService;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping("api/productos")
@Getter
public class ProductoController {

    private final ProductoService productoService;
    
    private ImageService imageService;

    public ProductoController(ProductoService productoService, ImageService imageService) {
        this.productoService = productoService;
        this.imageService = imageService;
    }

    /**
     * Obtener todos los productos
     */
    @GetMapping
    public ResponseEntity<?> getAllProductos() {
        List<Producto> productos = productoService.getAll();

        return ResponseEntity.ok(
            new ApiResponseBody.Builder("Lista de productos obtenida con éxito.")
                .status(ResponseStatus.SUCCESS)
                .data(productos)
                .build()
        );
    }
    
    /**
     * Obtener productos por categoría
     */
    @GetMapping(params = "categoriaId")
    public ResponseEntity<?> getProductosByCategoria(@RequestParam Long categoriaId) {
        List<Producto> productos;
        try {
            productos = productoService.getProductosByCategoria(categoriaId);
        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
        }

        return ResponseEntity.ok(
            new ApiResponseBody.Builder("Productos de la categoría obtenidos con éxito.")
                .status(ResponseStatus.SUCCESS)
                .data(productos)
                .build()
        );
    }


    /**
     * Obtener un producto por ID
     */
    @GetMapping(params = "id")
    public ResponseEntity<?> getProductoById(@RequestParam Long id) {
    	Optional<Producto> producto;
        try {
             producto = productoService.getProducto(id);

        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
        }
        
        return ResponseEntity.ok(new ApiResponseBody.Builder("Producto encontrado con éxito.")
                .status(ResponseStatus.SUCCESS)
                .data(producto)
                .build());
    }

    /**
     * Crear un nuevo producto
     */
    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody Producto producto) {
        try {
            productoService.createProducto(producto);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage()).build();
        } catch(EntityNotFoundException e) {
        	throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseBody.Builder("Producto creado correctamente.")
                .status(ResponseStatus.SUCCESS)
                .build());
    }

    /**
     * Actualizar un producto existente
     */
    @PutMapping
    public ResponseEntity<?> updateProducto(@RequestBody Producto producto) {
        try {
            productoService.updateProducto(producto);
        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage())
            	.build();
        } catch(IllegalStateException | IllegalArgumentException e) {
        	throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
        	.build();
        }
        
        return ResponseEntity.ok(new ApiResponseBody.Builder("Producto actualizado correctamente.")
                .status(ResponseStatus.SUCCESS)
                .build());
    }
    
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ApiResponseBody> uploadImage(@PathVariable Long id, 
                                                       @RequestParam MultipartFile file) {
        try {
            Optional<Producto> optionalProducto = productoService.getProducto(id);
            if (!optionalProducto.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseBody.Builder("No se ha encontrado el producto que buscas.")
                                .status(ResponseStatus.ERROR)
                                .build());
            }
            Producto producto = optionalProducto.get();

            String imageUrl = imageService.persistImage(file);

            producto.setImgUrl(imageUrl);
            producto.setImgOrigen(ImagenOrigen.UPLOAD);

            Producto updatedProducto = productoService.updateProducto(producto);

            return ResponseEntity.ok(new ApiResponseBody.Builder("Imagen de producto subida y asociada correctamente.")
                    .status(ResponseStatus.SUCCESS)
                    .data(updatedProducto)
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
