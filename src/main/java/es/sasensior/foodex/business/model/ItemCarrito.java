package es.sasensior.foodex.business.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
public class ItemCarrito {
	
	@JsonBackReference
	private CarritoCompra carrito;
	
	private Producto producto;
	
	private Integer cantidad;
}
