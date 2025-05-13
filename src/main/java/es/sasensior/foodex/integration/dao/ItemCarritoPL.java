package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ITEMS_CARRITO")
@IdClass(ItemCarritoIdPL.class)
public class ItemCarritoPL {
	
	@Id
	@ManyToOne
    @JoinColumn(name = "ID_CARRITO", nullable = false)
	private CarritoCompraPL carrito;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "ID_PRODUCTO", nullable = false)
	private ProductoPL producto;
	
	@Column(nullable = false)
	private Integer cantidad;

}