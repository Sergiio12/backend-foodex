package es.sasensior.foodex.integration.dao;

import java.util.Date;

import es.sasensior.foodex.business.model.ImagenOrigen;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODUCTOS")
public class ProductoPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST) 
	@JoinColumn(name = "ID_CATEGORIA", nullable = false)
	private CategoriaPL categoria;
	
	@NotBlank
	private String nombre;
	
	private String descripcion;
	
	@NotNull
	private Double precio;
	
	@NotNull
	private Integer stock;
	
	@NotNull
	private Boolean descatalogado;
	
	private String imgUrl;
	
	@NotNull
	private Date fechaAlta;

	@Enumerated(EnumType.STRING)
    private ImagenOrigen imgOrigen;
	
}