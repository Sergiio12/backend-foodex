package es.sasensior.foodex.business.model;

import es.sasensior.foodex.security.integration.model.UsuarioPL;
import lombok.Data;

@Data
public class Cliente {
	private Long id;
	private UsuarioPL usuario;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String telefono;
	private String email;
	private String codPostal;
	private String provincia;
	private String calle;
	private String bloque;
	private String portal;

}
