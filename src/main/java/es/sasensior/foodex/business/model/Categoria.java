package es.sasensior.foodex.business.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Categoria {
	private Long id;
	private String nombre;
	private String descripcion;
	private String imgUrl;
	private String imgLocalPath;
	private LocalDateTime fechaAlta;
	private LocalDateTime ultimaFechaActualizacion;
	private Boolean descatalogada;
	
}
