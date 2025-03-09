package es.sasensior.foodex.presentation.restcontrollers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.sasensior.foodex.business.model.CarritoCompra;
import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping(path = "api/carrito")
@Getter
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<?> getCarrito() {
        Optional<CarritoCompra> carrito = this.carritoService.getCarrito();
        if (carrito.isPresent()) {
            return ResponseEntity.ok(new ApiResponseBody.Builder("Productos del carrito obtenidos con éxito")
            		.status(ResponseStatus.SUCCESS)
            		.data(carrito.get())
            		.build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseBody.Builder("No hemos podido localizar su carrito de la compra. Por favor, contacta con un administrador para más información.")
                    .status(ResponseStatus.ERROR)
                    .build());
        }
    }

    @PutMapping("/añadir")
    public ResponseEntity<?> addProductoToCarrito(@RequestParam Long idProducto, @RequestParam Integer cantidad) {
        try {
            carritoService.addProductoToCarrito(idProducto, cantidad);
        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
        } catch (IllegalStateException e) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage()).build();
        }

        return ResponseEntity.ok(
            new ApiResponseBody.Builder("Producto añadido correctamente al carrito.")
                .status(ResponseStatus.SUCCESS)
                .build()
        );
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> removeProductoFromCarrito(@RequestParam Long idProducto, @RequestParam Integer cantidad) {
        try {
            carritoService.removeProductoFromCarrito(idProducto, cantidad);
        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage()).build();
        }

        return ResponseEntity.ok(
            new ApiResponseBody.Builder("Producto eliminado del carrito.")
                .status(ResponseStatus.SUCCESS)
                .build()
        );
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<?> removeAllProductosFromCarrito() {
        try {
            this.carritoService.removeAllProductosFromCarrito();
        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage()).build();
        }

        return ResponseEntity.ok(
            new ApiResponseBody.Builder("Se ha vaciado el carrito de la compra.")
                .status(ResponseStatus.SUCCESS)
                .build()
        );
    }
}
