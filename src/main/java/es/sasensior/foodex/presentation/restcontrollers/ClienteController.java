package es.sasensior.foodex.presentation.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sasensior.foodex.business.services.ClienteService;
import lombok.Getter;

@RestController
@RequestMapping(path = "/api/clientes")
@Getter
public class ClienteController {
	
	private final ClienteService clienteService;
	
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(clienteService.getAll());
	}

}
