package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@NotBlank
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	@Embedded
	private DireccionPL direccion;

	@Embedded
	private DatosContactoPL datosContacto;
	
}
