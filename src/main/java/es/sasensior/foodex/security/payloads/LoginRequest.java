package es.sasensior.foodex.security.payloads;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Debes especificar tu nombre de usuario!")
	private String username;
	
	@NotBlank(message="Debes especificar tu contraseña!")
	private String password;

}
