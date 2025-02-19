package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class Direccion {
	private String codPostal;
	private String provincia;
	private String calle;
	private String bloque;
	private String portal;
	
}
