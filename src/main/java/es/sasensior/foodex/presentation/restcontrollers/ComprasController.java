package es.sasensior.foodex.presentation.restcontrollers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.business.model.DatosContacto;
import es.sasensior.foodex.business.model.Direccion;
import es.sasensior.foodex.business.model.dto.CompraDTO;
import es.sasensior.foodex.business.model.dto.CompraRequestDTO;
import es.sasensior.foodex.business.services.CompraService;
import es.sasensior.foodex.presentation.utils.ApiResponseBody;
import es.sasensior.foodex.presentation.utils.PresentationException;
import es.sasensior.foodex.presentation.utils.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@Getter
@RestController
@RequestMapping(path = "api/compras")
public class ComprasController {
	
	private final CompraService compraService;
	
	public ComprasController(CompraService compraService) {
		this.compraService = compraService;
	}
	
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCompras() {
    	return ResponseEntity.ok(new ApiResponseBody.Builder("Lista de compras:")
    		.status(ResponseStatus.SUCCESS)
    		.data(this.compraService.getAllComprasDTO())
    		.build());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = "id")
    public ResponseEntity<?> getCompra(@RequestParam Long id) {
        Optional<CompraDTO> compraDTO;
        try {
            compraDTO = compraService.getCompraDTO(id);
            
            return ResponseEntity.ok(new ApiResponseBody.Builder("Compra encontrada con éxito.")
        		.status(ResponseStatus.SUCCESS)
        		.data(compraDTO.get())
        		.build());

        } catch (IllegalStateException e) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
                .data(null)
                .build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(params = "id")
    public ResponseEntity<?> deleteCompra(@RequestParam Long id) {
    	try {
    		compraService.removeCompra(id);
    		
    		return ResponseEntity.ok(new ApiResponseBody.Builder("Compra borrada con éxito")
    				.status(ResponseStatus.SUCCESS)
    				.build());
    	} catch(IllegalStateException e) {
    		throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
    			.build();
    	}
    }
    
    @PostMapping
    public ResponseEntity<?> comprar(@RequestBody CompraRequestDTO compraRequestDTO) {
        try {
            String comentario = compraRequestDTO.getComentario();
            Direccion direccion = compraRequestDTO.getDireccion();
            DatosContacto datosContacto = compraRequestDTO.getDatosContacto();

            CompraDTO compraDTO = this.compraService.realizarCompra(comentario, direccion, datosContacto);

            return ResponseEntity.ok(new ApiResponseBody.Builder("Compra realizada con éxito.")
                    .status(ResponseStatus.SUCCESS)
                    .data(compraDTO)
                    .build());

        } catch (EntityNotFoundException e) {
            throw new PresentationException.Builder(HttpStatus.NOT_FOUND, e.getMessage())
                    .build();
        } catch (IllegalStateException e) {
            throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
                    .build();
        }
    }


}
