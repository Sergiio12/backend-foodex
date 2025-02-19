package es.sasensior.foodex.security.payloads;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="Es necesario especificar un nombre de usuario!")
	@Size(min = 4, max = 50, message = "El nombre de usuario debe tener entre 4 y 50 caracteres")
	private String username;
	
	@NotBlank(message="Es necesario especificar un correo electrónico!")
	@Email(message = "El correo debe tener un formato válido")
	private String email;
	
	@NotBlank(message="Es necesario espeficiar una contraseña!")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	private String password;
	
	@NotBlank(message="Es necesario espeficiar un nombre!")
	private String name;
	
	@NotBlank(message="Es necesario espeficiar un nombre!")
	private String firstName;
	
	@NotBlank(message="Es necesario espeficiar un nombre!")
	private String lastName;

}
