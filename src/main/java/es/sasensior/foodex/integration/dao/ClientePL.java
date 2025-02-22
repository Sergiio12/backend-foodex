package es.sasensior.foodex.integration.dao;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "CLIENTES")
@SequenceGenerator(name = "GENERAL_SEQ", sequenceName = "GENERAL_SEQ", allocationSize = 1)
public class ClientePL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_SEQ")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false, unique = true)
	private UsuarioPL usuario;
	
	@NotBlank
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	@NotBlank
	private String telefono;
	
	private String email;
	
	private String codPostal;
	
	@NotBlank
	private String provincia;
	
	private String calle;
	
	private String bloque;
	
	private String portal;

}
