package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class ProductosCompra {
	private Compra compra;
	private Producto producto;
	private Integer cantidad;
	private Double precio;

}
