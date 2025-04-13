package es.sasensior.foodex.security.payloads;

import es.sasensior.foodex.business.model.ImagenOrigen;
import lombok.Data;

@Data
public class CategoriaUpdateRequest {
	private String nombre;
	private String descripcion;
	private ImagenOrigen imgOrigen;
}
