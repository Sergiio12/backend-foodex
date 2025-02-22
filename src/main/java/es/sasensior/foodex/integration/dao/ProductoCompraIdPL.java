package es.sasensior.foodex.integration.dao;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductoCompraIdPL implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long compra;
	private Long producto;

}
