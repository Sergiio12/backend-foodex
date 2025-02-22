package es.sasensior.foodex.business.model;

import java.util.List;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import lombok.Data;

@Data
public class CarritoCompra {
	private Long id;
	private UsuarioPL usuario;
	private List<ItemCarrito> items;

}
