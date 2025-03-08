package es.sasensior.foodex.presentation.restcontrollers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.business.services.CarritoService;
import es.sasensior.foodex.integration.dao.CarritoCompraPL;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
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
	    Optional<CarritoCompraPL> carrito = this.carritoService.getCarrito();
	    if (carrito.isPresent()) {
	        return ResponseEntity.ok(carrito.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ApiResponseBody.Builder("Usted no tiene carrito de la compra asignado. Contacta con un administrador o revise su información.")
	                .status(ResponseStatus.ERROR)
	                .build());
	    }
	}

}
