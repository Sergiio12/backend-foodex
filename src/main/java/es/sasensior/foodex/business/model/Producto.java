package es.sasensior.foodex.business.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Producto {
	private Long id;
	private String nombre;
	private String descripcion;
	private Integer stock;
	private Float precio;
	private String imgUrl;
	private String imgLocalPath;
	private Categoria categoria;
	private LocalDateTime fechaAlta;
	private LocalDateTime ultimaFechaActualizacion;
	private Boolean descatalogado;

}
