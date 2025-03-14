package es.sasensior.foodex.business.model.dto;

import es.sasensior.foodex.business.model.DatosContacto;
import es.sasensior.foodex.business.model.Direccion;
import lombok.Data;

@Data
public class CompraRequestDTO {
	private String comentario;
	private Direccion direccion;
	private DatosContacto datosContacto;

}
