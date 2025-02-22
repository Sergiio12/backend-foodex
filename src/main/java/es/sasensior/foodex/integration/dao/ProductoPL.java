package es.sasensior.foodex.integration.dao;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODUCTOS")
@SequenceGenerator(name = "GENERAL_SEQ", sequenceName = "GENERAL_SEQ", allocationSize = 1)
public class ProductoPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_SEQ")
	private Long id;
	
	@ManyToOne
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

}
