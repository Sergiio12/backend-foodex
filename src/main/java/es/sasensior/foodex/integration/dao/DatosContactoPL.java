package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Embeddable
public class DatosContactoPL {
	
	@NotBlank
	private String telefono;
	
	@Email
	private String email;

}
