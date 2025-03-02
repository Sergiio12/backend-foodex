package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class Cliente {
	private Long id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private DatosContacto datosContacto;
	private Direccion direccion;

}
