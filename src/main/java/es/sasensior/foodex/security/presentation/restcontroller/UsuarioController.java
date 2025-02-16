package es.sasensior.foodex.security.presentation.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.presentation.config.ApiResponseBody;
import es.sasensior.foodex.presentation.config.PresentationException;
import es.sasensior.foodex.presentation.config.ResponseStatus;
import es.sasensior.foodex.security.services.UsuarioPLService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
	
	private final UsuarioPLService usuarioPLService;
	
	public UsuarioController(UsuarioPLService usuarioPLService) {
		this.usuarioPLService = usuarioPLService;
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> asignarRol(@RequestParam String nombreUsuario, @RequestParam String nombreRol) {
		try {
			usuarioPLService.addRol(nombreUsuario, nombreRol);
			return ResponseEntity.ok(new ApiResponseBody.Builder("Rol asignado con éxito.")
					.status(ResponseStatus.SUCCESS)
					.build());
		} catch(IllegalStateException | IllegalArgumentException e) {
			throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
			.build();
		}
	}
	
    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping
	public ResponseEntity<?> eliminarRol(@RequestParam String nombreUsuario, @RequestParam String nombreRol) {
		try {
			usuarioPLService.removeRol(nombreUsuario, nombreRol);
			return ResponseEntity.ok(new ApiResponseBody.Builder("Rol eliminado con éxito.")
					.status(ResponseStatus.SUCCESS)
					.build());
		} catch(IllegalStateException | IllegalArgumentException e) {
			throw new PresentationException.Builder(HttpStatus.BAD_REQUEST, e.getMessage())
			.build();
		}
	}

}
