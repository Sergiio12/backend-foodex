package es.sasensior.foodex.integration.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CATEGORIAS")
@SequenceGenerator(name = "CATEGORIAS_SEQ", sequenceName = "CATEGORIAS_SEQ", allocationSize = 1)
public class CategoriaPL {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIAS_SEQ")
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	private String descripcion;
	
	private String imgUrl;

}
