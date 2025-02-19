package es.sasensior.foodex.business.model;

import lombok.Data;

@Data
public class ItemCarrito {
	private CarritoCompra carrito;
	private Producto producto;
}
