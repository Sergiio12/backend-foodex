package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODUCTOS_COMPRAS")
@IdClass(ProductoCompraIdPL.class)
public class ProductoCompraPL {
	
	@Id
	@ManyToOne
    @JoinColumn(name = "ID_COMPRA", nullable = false)
	private CompraPL compra;
	
	@Id
	@ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
	private ProductoPL producto;
	
	@NotNull
	private Integer cantidad;
	
	private Double precio;

}
