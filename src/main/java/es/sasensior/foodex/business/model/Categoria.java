package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class Categoria {
	private Long id;
	private String nombre;
	private String descripcion;
	private String imgUrl;
	
}
