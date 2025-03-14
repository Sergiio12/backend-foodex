package es.sasensior.foodex.business.model.dto;

import es.sasensior.foodex.business.model.DatosContacto;
import es.sasensior.foodex.business.model.Direccion;
import lombok.Data;

@Data
public class UsuarioDTO {
	private String nombre;
	private String apellido1;
	private String apellido2;
	private DatosContacto datosContacto;
	private Direccion direccion;

}
