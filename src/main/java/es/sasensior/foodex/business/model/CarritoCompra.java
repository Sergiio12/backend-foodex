package es.sasensior.foodex.business.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.sasensior.foodex.security.integration.dao.UsuarioPL;
import lombok.Data;

@Data
public class CarritoCompra {
	
	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	private UsuarioPL usuario;
	
	@JsonManagedReference
	private List<ItemCarrito> itemsCarrito;

}
