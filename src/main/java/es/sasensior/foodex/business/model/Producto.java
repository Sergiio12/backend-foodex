package es.sasensior.foodex.business.model;

import java.util.Date;

import lombok.Data;

@Data
public class Producto {
	private Long id;
	private Categoria categoria;
	private String nombre;
	private String descripcion;
	private Double precio;
	private Integer stock;
	private Boolean descatalogado;
	private String imgUrl;
	private ImagenOrigen imgOrigen;
	private Date fechaAlta;

}
