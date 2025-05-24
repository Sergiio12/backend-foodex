package es.sasensior.foodex.integration.dao;

import es.sasensior.foodex.business.model.ImagenOrigen;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CATEGORIAS")
public class CategoriaPL {
	
	@Id
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	private String descripcion;
	
	private String imgUrl;
	
	@Enumerated(EnumType.STRING)
    private ImagenOrigen imgOrigen;

}