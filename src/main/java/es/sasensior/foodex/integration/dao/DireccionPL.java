package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Embeddable
public class DireccionPL {
	
	@NotBlank
	private String codPostal;
	
	private String provincia;
	
	@NotBlank
	private String calle;
	
	@NotBlank
	private String bloque;
	
	@NotBlank
	private String portal;

}
