package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class Direccion {
	private String provincia;
	private String ciudad;
	private String codigoPostal;
	private String calle;
	private Short bloque;
	private Short portal;
	
}
